package net.Gmaj7.interestingmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public interface ModDamageTypes extends DamageTypes {
    ResourceKey<DamageType> SUKUNA = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("Sukuna"));

    static void bootstrap(BootstapContext<DamageType> pContext) {
        pContext.register(SUKUNA, new DamageType("Sukuna", 0.1F));
    }
}