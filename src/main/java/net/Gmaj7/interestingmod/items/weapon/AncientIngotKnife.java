package net.Gmaj7.interestingmod.items.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.Gmaj7.interestingmod.enchantment.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class AncientIngotKnife extends Item {
    public static float BASE_DAMAGE = 5.0F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public AncientIngotKnife(Properties pProperties) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        EquipmentSlot[] equipmentSlots = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
        boolean flag = true;
        for (int i = 0; i < 2; i++) {
            if (!pTarget.getItemBySlot(equipmentSlots[i]).isEmpty()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            float damage = (float) pAttacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue() + (float) EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, pStack) * 0.5F + pTarget.getMaxHealth() * 0.2F;
            int rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),pStack);
            if(rank == 2) damage = damage - pTarget.getMaxHealth() * 0.15F;
            pTarget.hurt(pTarget.getLastDamageSource(), damage);
            pStack.hurtAndBreak(1, pAttacker, p_43296_ -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(stack.getItem() == this && (enchantment == Enchantments.SHARPNESS
                                        || enchantment == Enchantments.UNBREAKING
                                        || enchantment == ModEnchantments.ARMYDESTROYER.get()
                                        || enchantment == Enchantments.MOB_LOOTING
                                        || enchantment == Enchantments.SMITE
                                        || enchantment == Enchantments.BANE_OF_ARTHROPODS
                                        || enchantment == Enchantments.KNOCKBACK
                                        || enchantment == Enchantments.FIRE_ASPECT))
            return true;
        else return false;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 25;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
}