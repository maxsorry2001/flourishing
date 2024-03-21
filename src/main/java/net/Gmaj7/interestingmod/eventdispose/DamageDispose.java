package net.Gmaj7.interestingmod.eventdispose;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.enchantment.ModEnchantments;
import net.Gmaj7.interestingmod.modeffect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

@Mod.EventBusSubscriber(modid = InterestingMod.MODID)
public class DamageDispose {
    @SubscribeEvent
    public static void remove(LivingAttackEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                int rank = 0;
                if(direct == source)
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                else if (direct instanceof ThrownTrident)
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                    else if (((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND));
                }
                if (rank > 0) {
                    EquipmentSlot[] equipmentSlot = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
                            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
                    for (int i = 0; i < 6; i++) {
                        ItemStack itemStack = (target.getItemBySlot(equipmentSlot[i]));
                        if (!itemStack.isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), itemStack);
                            target.setItemSlot(equipmentSlot[i], ItemStack.EMPTY);
                            target.level().addFreshEntity(itemEntity);
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void hurtadd(LivingHurtEvent event){
        if (!event.getEntity().level().isClientSide()) {
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                int rank = 0;
                if(direct == source)
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                else if (direct instanceof ThrownTrident)
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                    else if (((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND));
                }
                if (rank == 2) {
                    EquipmentSlot[] equipmentSlot = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
                    int phand = 0;
                    int thand = 0;
                    for (int i = 0; i < 2; i++) {
                        if(!((LivingEntity) source).getItemBySlot(equipmentSlot[i]).isEmpty())
                            phand ++;
                        if(!target.getItemBySlot(equipmentSlot[i]).isEmpty())
                            thand ++;
                    }
                    if(phand > thand)
                        event.setAmount(event.getAmount() + target.getMaxHealth() * 0.1F);
                }
                if(((LivingEntity) source).hasEffect(ModEffects.WINE.get())){
                    event.setAmount(event.getAmount() * 2);
                }
            }
        }
    }
}