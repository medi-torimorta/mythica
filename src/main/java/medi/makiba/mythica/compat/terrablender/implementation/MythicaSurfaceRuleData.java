package medi.makiba.mythica.compat.terrablender.implementation;

import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.worldgen.TBSurfaceRuleData;

public class MythicaSurfaceRuleData {

    public static SurfaceRules.RuleSource mythica() {
        return TBSurfaceRuleData.overworldLike(true, false, true);
    }
}