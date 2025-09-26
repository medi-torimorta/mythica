package medi.makiba.mythica.event;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.Client.Particle.MythicaPortalParticle;
import medi.makiba.mythica.block.portal.MythicaPortalVisuals;
import medi.makiba.mythica.registry.MythicaBlocks;
import medi.makiba.mythica.registry.MythicaParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Mythica.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT)
public class ModClientEvents {
    
    @SubscribeEvent
    private static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MythicaParticleTypes.MYTHICA_PORTAL.get(), MythicaPortalParticle.Provider::new);
    }

    @SubscribeEvent
    private static void mythicaPortalFOV(ComputeFovModifierEvent event) {
		if (MythicaPortalVisuals.getPortalAnimTime() > 0.0F) {
			event.setNewFovModifier(event.getFovModifier() - MythicaPortalVisuals.getPortalAnimTime());
		}
	}

    @SubscribeEvent
    private static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Mythica.MODID, "mythica_portal_overlay"), (guiGraphics, deltaTracker) -> {
			Minecraft minecraft = Minecraft.getInstance();
			Window window = minecraft.getWindow();
			LocalPlayer player = minecraft.player;

			if (player != null) {
				renderPortalOverlay(guiGraphics, minecraft, window, deltaTracker.getGameTimeDeltaPartialTick(true));
			}
		});
    }

    private static void renderPortalOverlay(GuiGraphics graphics, Minecraft minecraft, Window window, float partialTicks) {
		float alpha = Mth.lerp(partialTicks, MythicaPortalVisuals.getPrevPortalAnimTime(), MythicaPortalVisuals.getPortalAnimTime());
		if (alpha > 0.0F) {
			if (alpha < 1.0F) {
				alpha = alpha * alpha;
				alpha = alpha * alpha;
				alpha = alpha * 0.8F + 0.2F;
			}

			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			graphics.setColor(1.0F, 1.0F, 1.0F, alpha);
			TextureAtlasSprite textureatlassprite = minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(MythicaBlocks.MYTHICA_PORTAL.get().defaultBlockState());
			graphics.blit(0, 0, -90, window.getGuiScaledWidth(), window.getGuiScaledHeight(), textureatlassprite);
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

}