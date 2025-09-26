package medi.makiba.mythica.registry;

import medi.makiba.mythica.Mythica;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MythicaTags {
    public static class Blocks {
        public static final TagKey<Block> PORTAL_FRAME_BLOCKS = tag("portal_frame_blocks");
        public static final TagKey<Block> PORTAL_REPLACEABLE = tag("portal_replaceable");
    
        private static TagKey<Block> tag(String name) {
			return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Mythica.MODID, name));
        }
    }
}
