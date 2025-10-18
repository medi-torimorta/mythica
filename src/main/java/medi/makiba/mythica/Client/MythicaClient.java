package medi.makiba.mythica.Client;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.registry.MythicaSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Mythica.MODID, dist = Dist.CLIENT)
public class MythicaClient {
    public MythicaClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void playPortalSound() {
		Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forLocalAmbience(MythicaSoundEvents.MYTHICA_PORTAL_TRAVEL.get(), 1.0F, 1.0F));
	}
}
