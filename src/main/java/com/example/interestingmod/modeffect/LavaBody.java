package com.example.interestingmod.modeffect;

import com.example.interestingmod.InterestingMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import static net.minecraft.world.level.material.Fluids.FLOWING_WATER;
import static net.minecraft.world.level.material.Fluids.WATER;

@Mod.EventBusSubscriber(modid = InterestingMod.MODID)
public class LavaBody extends MobEffect {
    protected LavaBody(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @SubscribeEvent
    public static void effect(LivingEvent.LivingTickEvent event){
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        if(livingEntity.hasEffect(ModEffects.LAVA_BODY.get())){
            if(livingEntity.wasOnFire && !livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE))
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,100));
            BlockPos blockPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY() - 1,livingEntity.getBlockZ());
            if(level.getFluidState(blockPos).is(WATER))
                level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
            if(level.getFluidState(blockPos).is(FLOWING_WATER))
                level.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
        }
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return super.shouldApplyEffectTickThisTick(pDuration, pAmplifier);
    }
}
