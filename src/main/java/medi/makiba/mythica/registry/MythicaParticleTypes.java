package medi.makiba.mythica.registry;

import medi.makiba.mythica.Mythica;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MythicaParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, Mythica.MODID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MYTHICA_PORTAL = PARTICLES.register("mythica_portal", () -> new SimpleParticleType(false));
}
