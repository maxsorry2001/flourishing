package net.Gmaj7.flourishing.flourishingDamageType;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public interface FlourishingDamageTypes extends DamageTypes {
    ResourceKey<DamageType> HEALTH_LOOSE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("health_loose"));

    static void bootstrap(BootstapContext<DamageType> pContext) {
        pContext.register(HEALTH_LOOSE, new DamageType("health_loose", 0.1F, DamageEffects.HURT));
    }
}
