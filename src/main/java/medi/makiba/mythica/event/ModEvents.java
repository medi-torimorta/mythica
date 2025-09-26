package medi.makiba.mythica.event;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.block.portal.MythicaPortalVisuals;
import medi.makiba.mythica.network.MythicaPortalSoundPacket;
import net.neoforged.bus.api.SubscribeEvent;
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
}