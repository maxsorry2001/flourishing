package net.Gmaj7.interestingmod.modEnchantment;

import net.Gmaj7.interestingmod.modItems.weapon.AncientIngotKnife;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ArmyDestroyer extends Enchantment {
    protected ArmyDestroyer(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof AxeItem
                || pStack.getItem() instanceof SwordItem
                || pStack.getItem() instanceof TridentItem
                || pStack.getItem() instanceof BowItem
                || pStack.getItem() instanceof CrossbowItem
                || pStack.getItem() instanceof AncientIngotKnife;
    }
}
