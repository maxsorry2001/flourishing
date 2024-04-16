package net.Gmaj7.flourishing.flourishingEnchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ArrogantAndWilful extends Enchantment {
    protected ArrogantAndWilful(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
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
        return pStack.getItem() instanceof SwordItem
                || pStack.getItem() instanceof AxeItem
                || pStack.getItem() instanceof ProjectileWeaponItem
                || pStack.getItem() instanceof TridentItem;
    }
}