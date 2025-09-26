package medi.makiba.mythica.block.portal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import medi.makiba.mythica.MythicaConfig;
import medi.makiba.mythica.network.MythicaPortalSoundPacket;
import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.registry.MythicaPointOfInterests;

public class MythicaPortalForcer {

	private static final BlockState FRAME = !BuiltInRegistries.BLOCK.containsKey(Objects.requireNonNull(ResourceLocation.tryParse(MythicaConfig.RETURN_PORTAL_FRAME_BLOCK_ID.get()))) ? Blocks.STONE_BRICKS.defaultBlockState() : BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(MythicaConfig.RETURN_PORTAL_FRAME_BLOCK_ID.get())).defaultBlockState();

	public static final DimensionTransition.PostDimensionTransition PLAY_PORTAL_SOUND = MythicaPortalForcer::playPortalSound;

	private static void playPortalSound(Entity entity) {
		if (entity instanceof ServerPlayer player) {
			PacketDistributor.sendToPlayer(player, MythicaPortalSoundPacket.INSTANCE);
		}
	}

	public static Optional<BlockPos> findClosestPortalPosition(ServerLevel level, BlockPos exitPos, boolean isUndergarden, WorldBorder worldBorder) {
		PoiManager poimanager = level.getPoiManager();
		int i = isUndergarden ? 16 : 128;
		poimanager.ensureLoadedAndValid(level, exitPos, i);
		return poimanager.getInSquare(holder -> holder.is(MythicaPointOfInterests.MYTHICA_PORTAL), exitPos, i, PoiManager.Occupancy.ANY)
			.map(PoiRecord::getPos)
			.filter(worldBorder::isWithinBounds)
			.filter(pos -> level.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
			.min(Comparator.<BlockPos>comparingDouble(pos -> pos.distSqr(exitPos)).thenComparingInt(Vec3i::getY));
	}

	public static Optional<BlockUtil.FoundRectangle> createPortal(ServerLevel level, BlockPos pos, Direction.Axis axis) {
		Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		double d0 = -1.0;
		BlockPos portalPos = null;
		double d1 = -1.0;
		BlockPos backupPortalPos = null;
		WorldBorder worldBorder = level.getWorldBorder();
		int maxHeight = level.getMaxBuildHeight() - 1;
		BlockPos.MutableBlockPos framePos = pos.mutable();

		for (BlockPos.MutableBlockPos checkPos : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
			if (worldBorder.isWithinBounds(checkPos) && worldBorder.isWithinBounds(checkPos.move(direction, 1))) {
				checkPos.move(direction.getOpposite(), 1);

				for (int checkY = maxHeight; checkY >= 0; checkY--) {
					checkPos.setY(checkY);
					if (canPortalReplaceBlock(level, checkPos)) {
						int currentY = checkY;

						while (checkY > 0 && canPortalReplaceBlock(level, checkPos.move(Direction.DOWN))) {
							checkY--;
						}

						if (checkY + 4 <= maxHeight) {
							int portalGap = currentY - checkY;
							if (portalGap <= 0 || portalGap >= 3) {
								checkPos.setY(checkY);
								if (canHostFrame(level, checkPos, framePos, direction, 0)) {
									double dist = pos.distSqr(checkPos);
									if (canHostFrame(level, checkPos, framePos, direction, -1)
										&& canHostFrame(level, checkPos, framePos, direction, 1)
										&& (d0 == -1.0 || d0 > dist)) {
										d0 = dist;
										portalPos = checkPos.immutable();
									}

									if (d0 == -1.0 && (d1 == -1.0 || d1 > dist)) {
										d1 = dist;
										backupPortalPos = checkPos.immutable();
									}
								}
							}
						}
					}
				}
			}
		}

		if (d0 == -1.0 && d1 != -1.0) {
			portalPos = backupPortalPos;
			d0 = d1;
		}

		if (d0 == -1.0) {
			portalPos = new BlockPos(pos.getX() - direction.getStepX(), Mth.clamp(pos.getY(), 70, maxHeight - 9), pos.getZ() - direction.getStepZ()).immutable();
			portalPos = worldBorder.clampToBounds(portalPos);
			Direction direction1 = direction.getClockWise();

			for (int i3 = -1; i3 < 2; i3++) {
				for (int j3 = 0; j3 < 2; j3++) {
					for (int k3 = -1; k3 < 3; k3++) {
						BlockState blockstate1 = k3 < 0 ? FRAME : Blocks.AIR.defaultBlockState();
						framePos.setWithOffset(
							portalPos, j3 * direction.getStepX() + i3 * direction1.getStepX(), k3, j3 * direction.getStepZ() + i3 * direction1.getStepZ()
						);
						level.setBlockAndUpdate(framePos, blockstate1);
					}
				}
			}
		}

		for (int frameX = -1; frameX < 3; frameX++) {
			for (int frameY = -1; frameY < 4; frameY++) {
				if (frameX == -1 || frameX == 2 || frameY == -1 || frameY == 3) {
					framePos.setWithOffset(portalPos, frameX * direction.getStepX(), frameY, frameX * direction.getStepZ());
					level.setBlock(framePos, FRAME, 3);
				}
			}
		}

		BlockState portal = MythicaBlocks.MYTHICA_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);

		for (int portalX = 0; portalX < 2; portalX++) {
			for (int portalY = 0; portalY < 3; portalY++) {
				framePos.setWithOffset(portalPos, portalX * direction.getStepX(), portalY, portalX * direction.getStepZ());
				level.setBlock(framePos, portal, 18);
			}
		}

		return Optional.of(new BlockUtil.FoundRectangle(portalPos.immutable(), 2, 3));
	}

	private static boolean canPortalReplaceBlock(ServerLevel level, BlockPos.MutableBlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
	}

	private static boolean canHostFrame(ServerLevel level, BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction direction, int pOffsetScale) {
		Direction directionClockWise = direction.getClockWise();

		for (int i = -1; i < 3; i++) {
			for (int j = -1; j < 4; j++) {
				offsetPos.setWithOffset(
					originalPos, directionClockWise.getStepX() * i + directionClockWise.getStepX() * pOffsetScale, j, directionClockWise.getStepZ() * i + directionClockWise.getStepZ() * pOffsetScale
				);
				if (j < 0 && !level.getBlockState(offsetPos).isSolid()) {
					return false;
				}

				if (j >= 0 && !canPortalReplaceBlock(level, offsetPos)) {
					return false;
				}
			}
		}
		return true;
	}
}
