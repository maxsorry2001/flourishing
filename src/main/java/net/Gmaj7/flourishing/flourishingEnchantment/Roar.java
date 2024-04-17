package net.Gmaj7.flourishing.flourishingEnchantment;

import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffect;
import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Roar extends Enchantment {
    protected Roar(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof SwordItem || pStack.getItem() instanceof AxeItem || pStack.getItem() instanceof TridentItem;
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        pAttacker.addEffect(new MobEffectInstance(FlourishingEffects.ROAR.get(), 600, 0));
    }
}