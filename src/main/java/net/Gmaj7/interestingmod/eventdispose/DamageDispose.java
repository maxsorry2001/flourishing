package net.Gmaj7.interestingmod.eventdispose;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.modEnchantment.ModEnchantments;
import net.Gmaj7.interestingmod.modeffect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

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
    public static void damageAdd(LivingDamageEvent event){
        if (!event.getEntity().level().isClientSide()) {
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                int rank = 0;
                boolean aaf = false;
                if(direct == source){
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                    aaf = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND)) == 1;
                }
                else if (direct instanceof ThrownTrident){
                    rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                    aaf = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((ThrownTrident) direct).getPickupItemStackOrigin()) == 1;
                }
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                        aaf = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getMainHandItem()) == 1;
                    }
                    else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                        rank = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getOffhandItem());
                        aaf = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getOffhandItem()) == 1;
                    }
                }
                if (rank == 2) {
                    EquipmentSlot[] equipmentSlot = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
                            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
                    int phand = 0,pbody = 0,thand = 0,tbody = 0;
                    for (int i = 0; i < 6; i++) {
                        if(!((LivingEntity) source).getItemBySlot(equipmentSlot[i]).isEmpty())
                        {
                            if(i < 2) phand ++;
                            else pbody++;
                        }
                        if(!target.getItemBySlot(equipmentSlot[i]).isEmpty()){
                            if(i < 2) thand ++;
                            else tbody++;
                        }
                    }
                    if(phand > thand && pbody > tbody)
                        event.setAmount(event.getAmount() + Math.min(100F, target.getMaxHealth() * 0.2F));
                }
                if(aaf){
                    List<LivingEntity> list = source.level().getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(11D,11D,5D));
                    int shand = 0, thandmax = 0;
                    for (LivingEntity livingEntity : list){
                        int hand = 0;
                        if(!livingEntity.getMainHandItem().isEmpty()) hand++;
                        if(!livingEntity.getOffhandItem().isEmpty()) hand++;
                        if(livingEntity == source) shand = hand;
                        else if (hand > thandmax) thandmax = hand;
                    }
                    if (shand > thandmax) event.setAmount(event.getAmount() * 2.5F);
                }
                if(EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(), target.getMainHandItem()) == 1
                    || EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(), target.getOffhandItem()) == 1){
                    List<LivingEntity> list = target.level().getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(11D,11D,5D));
                    int thand = 0, ohandmax = 0;
                    for (LivingEntity livingEntity : list){
                        int hand = 0;
                        if(!livingEntity.getMainHandItem().isEmpty()) hand++;
                        if(!livingEntity.getOffhandItem().isEmpty()) hand++;
                        if(livingEntity == target) thand = hand;
                        else if (hand > ohandmax) ohandmax = hand;
                    }
                    if (thand > ohandmax) event.setAmount(event.getAmount() * 2.5F);
                }
            }
        }
    }
}