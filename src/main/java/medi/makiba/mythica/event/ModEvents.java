package medi.makiba.mythica.event;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.block.portal.MythicaPortalVisuals;
import medi.makiba.mythica.network.MythicaPortalSoundPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Mythica.MODID)
public class ModEvents {

    @SubscribeEvent
    private static void registerPackets(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(Mythica.MODID).versioned("1.0.0").optional();
		registrar.playToClient(MythicaPortalSoundPacket.TYPE, MythicaPortalSoundPacket.STREAM_CODEC, (payload, context) -> MythicaPortalSoundPacket.handle(context));
	}

	@SubscribeEvent
    public static void tickPortalLogic(PlayerTickEvent.Pre event) {
		if (event.getEntity().level().isClientSide()) {
	        MythicaPortalVisuals.handlePortalVisuals(event.getEntity());
		}
    }

	@SubscribeEvent
	public static void addFeaturePacks(final AddPackFindersEvent event) {
		event.addPackFinders(
			ResourceLocation.fromNamespaceAndPath("mythica", "use_mythica_biomes"),
			PackType.SERVER_DATA,
			Component.literal("Mythica: Use Mythica Biomes"),
			PackSource.FEATURE,
			false,
			Pack.Position.TOP
		);
	}
}