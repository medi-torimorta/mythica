package medi.makiba.mythica.network;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.Client.MythicaClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MythicaPortalSoundPacket implements CustomPacketPayload {

    public static final MythicaPortalSoundPacket INSTANCE = new MythicaPortalSoundPacket();
	public static final Type<MythicaPortalSoundPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "portal_sound"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MythicaPortalSoundPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(IPayloadContext context) {
		context.enqueueWork(MythicaClient::playPortalSound);
	}
    
}
