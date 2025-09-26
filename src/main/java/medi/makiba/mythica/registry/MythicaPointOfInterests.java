package medi.makiba.mythica.registry;

import com.google.common.collect.ImmutableSet;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class MythicaPointOfInterests {
	public static final DeferredRegister<PoiType> POI = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Mythica.MODID);
	public static final DeferredHolder<PoiType, PoiType> MYTHICA_PORTAL = POI.register("mythica_portal", () -> new PoiType(ImmutableSet.copyOf(MythicaBlocks.MYTHICA_PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));
}
