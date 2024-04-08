package net.Gmaj7.interestingmod.modEnchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class Company extends Enchantment {
    protected Company(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof ShovelItem
                || pStack.getItem() instanceof PickaxeItem
                || pStack.getItem() instanceof SwordItem;
    }
}