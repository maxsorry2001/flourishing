package net.Gmaj7.interestingmod.items;

import net.Gmaj7.interestingmod.modeffect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties WIND = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(0.5F)
            .alwaysEat()
            .fast()
            .effect(() -> new MobEffectInstance(ModEffects.WIND.get(),200),1.0F)
            .build();
}
