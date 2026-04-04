package medi.makiba.mythica.block.portal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.Level;
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

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.MythicaConfig;
import medi.makiba.mythica.network.MythicaPortalSoundPacket;
import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.registry.MythicaPointOfInterests;

public class MythicaPortalForcer {

	private static final int MIN_PORTAL_SEARCH_RANGE = 16;
	private static final int MAX_PORTAL_SEARCH_RANGE = 256;
	private static final int PORTAL_CREATION_MARGIN = 1;

	private static final BlockState FRAME = !BuiltInRegistries.BLOCK.containsKey(Objects.requireNonNull(ResourceLocation.tryParse(MythicaConfig.RETURN_PORTAL_FRAME_BLOCK_ID.get()))) ? Blocks.REINFORCED_DEEPSLATE.defaultBlockState() : BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(MythicaConfig.RETURN_PORTAL_FRAME_BLOCK_ID.get())).defaultBlockState();

	public static final DimensionTransition.PostDimensionTransition PLAY_PORTAL_SOUND = MythicaPortalForcer::playPortalSound;

	private static void playPortalSound(Entity entity) {
		if (entity instanceof ServerPlayer player) {
			PacketDistributor.sendToPlayer(player, MythicaPortalSoundPacket.INSTANCE);
		}
	}

	public static Optional<BlockPos> findClosestPortalPosition(ServerLevel level, double scale, BlockPos exitPos, WorldBorder worldBorder) {
		PoiManager poimanager = level.getPoiManager();
		int range = getPortalSearchRange(scale);
		poimanager.ensureLoadedAndValid(level, exitPos, range);
		return poimanager.getInSquare(holder -> holder.is(MythicaPointOfInterests.MYTHICA_PORTAL), exitPos, range, PoiManager.Occupancy.ANY)
			.map(PoiRecord::getPos)
			.filter(worldBorder::isWithinBounds)
			.filter(pos -> level.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
			.min(Comparator.<BlockPos>comparingDouble(pos -> pos.distSqr(exitPos)).thenComparingInt(Vec3i::getY));
	}

	public static Optional<BlockUtil.FoundRectangle> createPortal(ServerLevel level, BlockPos pos, Direction.Axis axis, double scale) {
		Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		double d0 = -1.0;
		BlockPos portalPos = null;
		double d1 = -1.0;
		BlockPos backupPortalPos = null;
		WorldBorder worldBorder = level.getWorldBorder();
		int maxHeight = level.getMaxBuildHeight() - 1;
		int searchRange = getPortalSearchRange(scale);
		int creationRange = getPortalCreationRange(searchRange);
		BlockPos.MutableBlockPos framePos = pos.mutable();

		for (BlockPos.MutableBlockPos checkPos : BlockPos.spiralAround(pos, creationRange, Direction.EAST, Direction.SOUTH)) {
			if (worldBorder.isWithinBounds(checkPos) && worldBorder.isWithinBounds(checkPos.move(direction, 1))) {
				checkPos.move(direction.getOpposite(), 1);

				for (int checkY = maxHeight; checkY >= level.getMinBuildHeight(); checkY--) {
					checkPos.setY(checkY);
					if (canPortalReplaceBlock(level, checkPos)) {
						int currentY = checkY;

						while (checkY > level.getMinBuildHeight() && canPortalReplaceBlock(level, checkPos.move(Direction.DOWN))) {
							checkY--;
						}

						if (checkY + 4 <= maxHeight) {
							int portalGap = currentY - checkY;
							if (portalGap <= 0 || portalGap >= 3) {
								checkPos.setY(checkY);
								if (isPortalWithinSearchRange(pos, checkPos, direction, creationRange) && canHostFrame(level, checkPos, framePos, direction, 0)) {
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
			portalPos = clampPortalToSearchRange(
				pos,
				new BlockPos(pos.getX() - direction.getStepX(), Mth.clamp(pos.getY(), 70, maxHeight - 9), pos.getZ() - direction.getStepZ()),
				direction,
				creationRange
			).immutable();
			portalPos = worldBorder.clampToBounds(portalPos);
			portalPos = clampPortalToSearchRange(pos, portalPos, direction, creationRange).immutable();
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

	private static int getPortalSearchRange(double scale) {
		return Math.min(Math.max((int)(16 * scale), MIN_PORTAL_SEARCH_RANGE), MAX_PORTAL_SEARCH_RANGE);
	}

	private static int getPortalCreationRange(int searchRange) {
		return Math.max(searchRange - PORTAL_CREATION_MARGIN, 1);
	}

	private static boolean isPortalWithinSearchRange(BlockPos originPos, BlockPos portalPos, Direction direction, int searchRange) {
		for (int portalX = 0; portalX < 2; portalX++) {
			int portalBlockX = portalPos.getX() + portalX * direction.getStepX();
			int portalBlockZ = portalPos.getZ() + portalX * direction.getStepZ();
			if (Math.abs(portalBlockX - originPos.getX()) > searchRange || Math.abs(portalBlockZ - originPos.getZ()) > searchRange) {
				return false;
			}
		}
		return true;
	}

	private static BlockPos clampPortalToSearchRange(BlockPos originPos, BlockPos portalPos, Direction direction, int searchRange) {
		int minX = originPos.getX() - searchRange - Math.min(direction.getStepX(), 0);
		int maxX = originPos.getX() + searchRange - Math.max(direction.getStepX(), 0);
		int minZ = originPos.getZ() - searchRange - Math.min(direction.getStepZ(), 0);
		int maxZ = originPos.getZ() + searchRange - Math.max(direction.getStepZ(), 0);
		int clampedX = Mth.clamp(portalPos.getX(), minX, maxX);
		int clampedZ = Mth.clamp(portalPos.getZ(), minZ, maxZ);
		return new BlockPos(clampedX, portalPos.getY(), clampedZ);
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
					originalPos, direction.getStepX() * i + directionClockWise.getStepX() * pOffsetScale, j, direction.getStepZ() * i + directionClockWise.getStepZ() * pOffsetScale
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

	public static ResourceKey<Level> ENTRANCE_DIM;
	static{
		ResourceLocation dimResLoc = ResourceLocation.tryParse(MythicaConfig.MYTHICA_PORTAL_DIMENSION.get());
		if(dimResLoc != null){
			ENTRANCE_DIM = ResourceKey.create(Registries.DIMENSION, dimResLoc);
		}else{
			ENTRANCE_DIM = Level.OVERWORLD;
			Mythica.LOGGER.warn("invalid dimension id for Mythica Portal Dimension in config, defaulting to overworld");
		}
	}
}
