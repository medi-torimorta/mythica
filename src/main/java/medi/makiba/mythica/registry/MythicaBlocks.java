package medi.makiba.mythica.registry;

import java.util.function.Supplier;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.block.portal.MythicaPortalBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MythicaBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Mythica.MODID);

    public static final DeferredBlock<Block> MYTHICA_PORTAL = registerBlock(
        "mythica_portal", () -> new MythicaPortalBlock()
        );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }
}
