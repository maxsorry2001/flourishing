package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.modEnchantment.ModEnchantments;
import net.Gmaj7.flourishing.modeffect.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class DamageDispose {

    static EquipmentSlot[] equipmentSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    @SubscribeEvent
    public static void remove(LivingAttackEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                if(((LivingEntity) source).hasEffect(ModEffects.BANISHMENT.get())) event.setCanceled(true);
                int armyDestroyer = 0;
                if(direct == source)
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                else if (direct instanceof ThrownTrident)
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND));
                    else if (((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.BOW || ((LivingEntity) source).getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.CROSSBOW)
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getItemBySlot(EquipmentSlot.OFFHAND));
                }
                if (armyDestroyer > 0) {
                    for (int i = 0; i < 6; i++) {
                        ItemStack itemStack = (target.getItemBySlot(equipmentSlots[i]));
                        if (!itemStack.isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), itemStack);
                            target.setItemSlot(equipmentSlots[i], ItemStack.EMPTY);
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
            float damageAdd = 0, damageMul = 0;
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                int armyDestroyer = 0;
                boolean aaw = false/*骄恣*/, rte = false/*却敌*/;
                if(direct == source){
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                    aaw = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getMainHandItem()) == 1;
                    rte = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getMainHandItem()) == 1;
                }
                else if (direct instanceof ThrownTrident){
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                    aaw = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((ThrownTrident) direct).getPickupItemStackOrigin()) == 1;
                    rte = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.REPULSE_THE_ENEMY.get(), ((ThrownTrident) direct).getPickupItemStackOrigin()) == 1;
                }
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                        aaw = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getMainHandItem()) == 1;
                        rte = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getMainHandItem()) == 1;
                    }
                    else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getOffhandItem());
                        aaw = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getOffhandItem()) == 1;
                        rte = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getOffhandItem()) == 1;
                    }
                }
                //破军
                if (armyDestroyer == 2) {
                    int phand = 0,pbody = 0,thand = 0,tbody = 0;
                    for (int i = 0; i < 6; i++) {
                        if(!((LivingEntity) source).getItemBySlot(equipmentSlots[i]).isEmpty())
                        {
                            if(i < 2) phand ++;
                            else pbody++;
                        }
                        if(!target.getItemBySlot(equipmentSlots[i]).isEmpty()){
                            if(i < 2) thand ++;
                            else tbody++;
                        }
                    }
                    if(phand > thand && pbody > tbody)
                        damageAdd += Math.min(100F, target.getMaxHealth() * 0.2F);
                }
                //骄恣
                if(aaw){
                    List<LivingEntity> list = source.level().getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(9D,5D,9D));
                    int shand = 0, thandmax = 0;
                    for (LivingEntity livingEntity : list){
                        int hand = 0;
                        if(!livingEntity.getMainHandItem().isEmpty()) hand++;
                        if(!livingEntity.getOffhandItem().isEmpty()) hand++;
                        if(livingEntity == source) shand = hand;
                        else if (hand > thandmax) thandmax = hand;
                    }
                    if (shand > thandmax)
                        damageMul += 0.75F;
                }
                if(EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(), target.getMainHandItem()) == 1
                    || EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.ARRORGANT_AND_WILFUL.get(), target.getOffhandItem()) == 1){
                    List<LivingEntity> list = target.level().getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(9D,5D,9D));
                    int thand = 0, ohandmax = 0;
                    for (LivingEntity livingEntity : list){
                        int hand = 0;
                        if(!livingEntity.getMainHandItem().isEmpty()) hand++;
                        if(!livingEntity.getOffhandItem().isEmpty()) hand++;
                        if(livingEntity == target) thand = hand;
                        else if (hand > ohandmax) ohandmax = hand;
                    }
                    if (thand > ohandmax)
                        damageMul += 0.75F;
                }
                //却敌
                if(rte && !((LivingEntity) source).hasEffect(ModEffects.REPULSE_THE_ENEMY.get())){
                    if(direct == source){
                        ((LivingEntity) source).getMainHandItem().setDamageValue(((LivingEntity) source).getMainHandItem().getDamageValue() + 10);
                    }
                    else if (direct instanceof ThrownTrident){
                        ((ThrownTrident) direct).getPickupItemStackOrigin().setDamageValue(((ThrownTrident) direct).getPickupItemStackOrigin().getDamageValue() + 10);
                    }
                    else if (direct instanceof AbstractArrow){
                        if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                            ((LivingEntity) source).getMainHandItem().setDamageValue(((LivingEntity) source).getMainHandItem().getDamageValue() + 10);
                        }
                        else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                            ((LivingEntity) source).getOffhandItem().setDamageValue(((LivingEntity) source).getOffhandItem().getDamageValue() + 10);
                        }
                    }
                    damageAdd += target.getHealth() * 0.3F;
                    ((LivingEntity) source).addEffect(new MobEffectInstance(ModEffects.REPULSE_THE_ENEMY.get(), 600));
                    if(source.isShiftKeyDown() && ((LivingEntity) source).getMaxHealth() > 2){
                        boolean equipFlag = false;
                        for (int i = 0; i < 6; i++){
                            if(!target.getItemBySlot(equipmentSlots[i]).isEmpty()) {
                                equipFlag = true;
                                break;
                            }
                        }
                        if(equipFlag){
                            int fliter = new Random().nextInt(6);
                            ItemStack getItem = target.getItemBySlot(equipmentSlots[fliter]);
                            for (int j = 0; j < 5 && getItem.isEmpty(); j++){
                                fliter = (fliter + 1) % 6;
                                getItem = target.getItemBySlot(equipmentSlots[fliter]);
                            }
                            if (!getItem.isEmpty()){
                                target.setItemSlot(equipmentSlots[fliter], ItemStack.EMPTY);
                                ItemEntity getItemEntity = new ItemEntity(source.level(), source.getX(), source.getY(), source.getZ(), getItem);
                                source.level().addFreshEntity(getItemEntity);
                                ((LivingEntity) source).addEffect(new MobEffectInstance(ModEffects.BACKWATER.get(), 600, ((LivingEntity) source).hasEffect(ModEffects.BACKWATER.get()) ? ((LivingEntity) source).getEffect(ModEffects.BACKWATER.get()).getAmplifier() + 1: 0));
                            }
                        }
                    }
                }
                //放逐
                if(EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.BANISH.get(), target.getItemBySlot(EquipmentSlot.CHEST)) > 0)
                    ((LivingEntity) source).addEffect(new MobEffectInstance(ModEffects.BANISHMENT.get(), 600,0));
            }
            event.setAmount((event.getAmount() + damageAdd) * (1F + damageMul));
        }
    }

   @SubscribeEvent
   public static void deathDeal(LivingDeathEvent event){
        Entity cause = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        LivingEntity target = event.getEntity();
        if (cause instanceof LivingEntity && !cause.level().isClientSide){
            boolean vegeance = false;
            int grave_digger = 0;
            if(direct == cause) {
                vegeance = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getMainHandItem()) > 0;
                grave_digger += EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GRAVE_DIGGER.get(),((LivingEntity) cause).getMainHandItem());
            }
            else if (direct instanceof ThrownTrident){
                    vegeance = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.VENGEANCE.get(), ((ThrownTrident) direct).getPickupItemStackOrigin()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GRAVE_DIGGER.get(), ((ThrownTrident) direct).getPickupItemStackOrigin());
            }
            else if (direct instanceof AbstractArrow){
                if(((LivingEntity) cause).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                    vegeance= EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getMainHandItem()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GRAVE_DIGGER.get(), ((LivingEntity) cause).getMainHandItem());
                }
                else if (((LivingEntity) cause).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                    vegeance= EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getOffhandItem()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.GRAVE_DIGGER.get(), ((LivingEntity) cause).getOffhandItem());
                }
            }
            //仇决
            if(vegeance){
                if(((LivingEntity) cause).hasEffect(ModEffects.REPULSE_THE_ENEMY.get()))
                    ((LivingEntity) cause).removeEffect(ModEffects.REPULSE_THE_ENEMY.get());
                ((LivingEntity) cause).addEffect(new MobEffectInstance(ModEffects.VENGEANCE.get(), 600 ,((LivingEntity) cause).hasEffect(ModEffects.VENGEANCE.get()) ? ((LivingEntity) cause).getEffect(ModEffects.VENGEANCE.get()).getAmplifier() + 1  : 0));
            }
            //行殇
            if(grave_digger > 0) {
                ResourceLocation resourceLocation = target.getLootTable();
                LootTable lootTable = target.level().getServer().getLootData().getLootTable(resourceLocation);
                LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel)target.level())
                        .withParameter(LootContextParams.THIS_ENTITY, target)
                        .withParameter(LootContextParams.ORIGIN, target.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, event.getSource())
                        .withOptionalParameter(LootContextParams.KILLER_ENTITY, cause)
                        .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, direct);
                if (cause instanceof Player) {
                    lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, (Player) cause)
                            .withLuck(2048F);
                    if(grave_digger == 2){
                        ((Player) cause).addEffect(new MobEffectInstance(MobEffects.HEAL,1,1));
                    }
                }

                LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
                lootTable.getRandomItems(lootparams, target.getLootTableSeed(), target::spawnAtLocation);
            }
        }
   }
}