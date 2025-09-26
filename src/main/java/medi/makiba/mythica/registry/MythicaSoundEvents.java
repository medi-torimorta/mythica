package medi.makiba.mythica.registry;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MythicaSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Mythica.MODID);
    
    public static final DeferredHolder<SoundEvent, SoundEvent> MYTHICA_PORTAL_AMBIENT = register("block.mythica_portal.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> MYTHICA_PORTAL_ACTIVATE = register("block.mythica_portal.activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> MYTHICA_PORTAL_TRAVEL = register("block.mythica_portal.travel");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Mythica.MODID, name)));
	}
}
