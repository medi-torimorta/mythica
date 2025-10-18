package medi.makiba.mythica.compat.terrablender.implementation;

import terrablender.api.SurfaceRuleManager;

public class MythicaRuleCategory {
    static {
        SurfaceRuleManager.RuleCategory.values();
    }
    public static SurfaceRuleManager.RuleCategory MYTHICA;
}
