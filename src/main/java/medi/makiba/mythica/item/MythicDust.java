package medi.makiba.mythica.item;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;
import java.util.function.Predicate;

import medi.makiba.mythica.block.portal.MythicaPortalShape;
import medi.makiba.mythica.registry.MythicaSoundEvents;
import medi.makiba.mythica.worldgen.dimension.MythicaDimensions;


public class MythicDust extends Item {
    public MythicDust() {
		super(new Properties()
			.stacksTo(1)
			.rarity(Rarity.RARE)
		);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getLevel().dimension() == Level.OVERWORLD || context.getLevel().dimension() == MythicaDimensions.MYTHICA_DIM) {
			BlockPos framePos = context.getClickedPos().relative(context.getClickedFace());
			Optional<MythicaPortalShape> optional = findPortalShape(context.getLevel(), framePos, shape -> shape.isValid() && shape.getPortalBlocks() == 0, Direction.Axis.X);
			if (optional.isPresent()) {
				optional.get().createPortalBlocks();
				context.getLevel().playSound(context.getPlayer(), context.getClickedPos(), MythicaSoundEvents.MYTHICA_PORTAL_ACTIVATE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
			}
		}
		return InteractionResult.FAIL;
	}

	public static Optional<MythicaPortalShape> findPortalShape(LevelAccessor accessor, BlockPos pos, Predicate<MythicaPortalShape> shape, Direction.Axis axis) {
		Optional<MythicaPortalShape> optional = Optional.of(new MythicaPortalShape(accessor, pos, axis)).filter(shape);
		if (optional.isPresent()) {
			return optional;
		} else {
			Direction.Axis oppositeAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
			return Optional.of(new MythicaPortalShape(accessor, pos, oppositeAxis)).filter(shape);
		}
	}
    
}
