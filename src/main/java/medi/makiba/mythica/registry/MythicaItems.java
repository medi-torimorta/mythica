package medi.makiba.mythica.registry;

import java.util.function.Supplier;

import medi.makiba.mythica.Mythica;
import medi.makiba.mythica.item.MythicDust;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;


public class MythicaItems {
   public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mythica.MODID);

   public static final Supplier<Item> MYTHIC_DUST = ITEMS.register("mythic_dust", MythicDust::new);
}
