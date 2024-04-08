package net.Gmaj7.interestingmod;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ModDamageSources {

    private final Registry<DamageType> damageTypes;
    private final DamageSource sukuna;
    public ModDamageSources(RegistryAccess pRegistry){
        this.damageTypes = pRegistry.registryOrThrow(Registries.DAMAGE_TYPE);
        this.sukuna = this.source(ModDamageTypes.SUKUNA);
    }
    public DamageSource source(ResourceKey<DamageType> pDamageTypeKey) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey));
    }

    public DamageSource source(ResourceKey<DamageType> pDamageTypeKey, @Nullable Entity pEntity) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey), pEntity);
    }

    public DamageSource source(ResourceKey<DamageType> pDamageTypeKey, @Nullable Entity pCausingEntity, @Nullable Entity pDirectEntity) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey), pCausingEntity, pDirectEntity);
    }

    public DamageSource sukuna(){
        return this.sukuna;
    }
}
