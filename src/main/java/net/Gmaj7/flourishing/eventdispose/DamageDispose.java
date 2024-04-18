package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingDamageType.FlourishingDamageTypes;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class DamageDispose {

    static EquipmentSlot[] equipmentSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    @SubscribeEvent
    public static void dealBegin(LivingAttackEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            if (source instanceof LivingEntity) {
                if(((LivingEntity) source).hasEffect(FlourishingEffects.BANISHMENT.get())) event.setCanceled(true);
                int armyDestroyer = 0;
                if(direct == source) {
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(), ((LivingEntity) source).getMainHandItem());
                }
                else if (direct instanceof ThrownTrident) {
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                }
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem ) {
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                    }
                    else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem) {
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getOffhandItem());
                    }
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
    public static void hurtDeal(LivingHurtEvent event){
        if (!event.getEntity().level().isClientSide()){
            Entity source = event.getSource().getEntity();
            Entity direct = event.getSource().getDirectEntity();
            LivingEntity target = event.getEntity();
            boolean unfeeling = false;
            if (source instanceof LivingEntity) {
                if(direct == source){
                    unfeeling = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.UNFEELING.get(), ((LivingEntity) source).getMainHandItem()) > 0;
                    if(unfeeling) ((LivingEntity) source).getMainHandItem().hurtAndBreak(1, (LivingEntity) source, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                else if (direct instanceof ThrownTrident){
                    unfeeling = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.UNFEELING.get(),((ThrownTrident) direct).getPickupItemStackOrigin()) > 0;
                }
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                        unfeeling = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.UNFEELING.get(),((LivingEntity) source).getMainHandItem()) > 0;
                    }
                    else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                        unfeeling = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.UNFEELING.get(),((LivingEntity) source).getOffhandItem()) > 0;
                    }
                    if(unfeeling) direct.remove(Entity.RemovalReason.CHANGED_DIMENSION);
                }
                if(unfeeling){
                    event.setCanceled(true);
                    Holder<DamageType> holder = target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FlourishingDamageTypes.HEALTH_LOOSE);
                    target.hurt(new DamageSource(holder), event.getAmount());
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
            int armyDestroyer = 0, roarRank = 0;
            boolean aaw = false/*骄恣*/, rte = false/*却敌*/;
            if (source instanceof LivingEntity) {
                if(direct == source){
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                    roarRank = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ROAR.get(),((LivingEntity) source).getMainHandItem());
                    aaw = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getMainHandItem()) == 1;
                    rte = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getMainHandItem()) == 1;
                }
                else if (direct instanceof ThrownTrident){
                    armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((ThrownTrident) direct).getPickupItemStackOrigin());
                    aaw = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(),((ThrownTrident) direct).getPickupItemStackOrigin()) == 1;
                    rte = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.REPULSE_THE_ENEMY.get(), ((ThrownTrident) direct).getPickupItemStackOrigin()) == 1;
                }
                else if (direct instanceof AbstractArrow){
                    if(((LivingEntity) source).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getMainHandItem());
                        aaw = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getMainHandItem()) == 1;
                        rte = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getMainHandItem()) == 1;
                    }
                    else if (((LivingEntity) source).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                        armyDestroyer = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARMYDESTROYER.get(),((LivingEntity) source).getOffhandItem());
                        aaw = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(),((LivingEntity) source).getOffhandItem()) == 1;
                        rte = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.REPULSE_THE_ENEMY.get(), ((LivingEntity) source).getOffhandItem()) == 1;
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
                if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(), target.getMainHandItem()) == 1
                    || EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.ARRORGANT_AND_WILFUL.get(), target.getOffhandItem()) == 1){
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
                if(rte && !((LivingEntity) source).hasEffect(FlourishingEffects.REPULSE_THE_ENEMY.get())){
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
                    ((LivingEntity) source).addEffect(new MobEffectInstance(FlourishingEffects.REPULSE_THE_ENEMY.get(), 600));
                    if(source.isShiftKeyDown() && ((LivingEntity) source).getMaxHealth() > 2){
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
                            ((LivingEntity) source).addEffect(new MobEffectInstance(FlourishingEffects.BACKWATER.get(), 600, ((LivingEntity) source).hasEffect(FlourishingEffects.BACKWATER.get()) ? ((LivingEntity) source).getEffect(FlourishingEffects.BACKWATER.get()).getAmplifier() + 1: 0));
                        }
                    }
                }
                //放逐
                if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.BANISH.get(), target.getItemBySlot(EquipmentSlot.CHEST)) > 0)
                    ((LivingEntity) source).addEffect(new MobEffectInstance(FlourishingEffects.BANISHMENT.get(), 600,0));
                //咆哮
                if(((LivingEntity) source).hasEffect(FlourishingEffects.ROAR.get()) && roarRank > 1){
                    switch (roarRank){
                        case 2 : {
                            damageAdd += target.getMaxHealth() * 0.20F;
                            break;
                        }
                        case 3 : {
                            damageAdd += target.getMaxHealth() * 0.30F;
                        }
                    }
                }
            }
            event.setAmount((event.getAmount() + damageAdd) * (1F + damageMul));
            //咆哮负面
            if(source instanceof LivingEntity && ((LivingEntity) source).hasEffect(FlourishingEffects.ROAR.get()) && roarRank > 1 && event.getAmount() < target.getHealth()){
                Holder<DamageType> holder = source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FlourishingDamageTypes.HEALTH_LOOSE);
                switch (roarRank){
                    case 2 : {
                        source.hurt(new DamageSource(holder), 2);
                        break;
                    }
                    case 3 : {
                        source.hurt(new DamageSource(holder), 4);
                        ((LivingEntity) source).getMainHandItem().hurtAndBreak(1, (LivingEntity) source, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                        break;
                    }
                }
            }
            //涅槃
            if(!target.hasEffect(FlourishingEffects.NIRVANA.get()) && EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.Nirvana.get(), target.getItemBySlot(EquipmentSlot.HEAD)) > 0 && event.getAmount() >= target.getHealth()){
                event.setAmount(0);
                target.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 7));
                target.addEffect(new MobEffectInstance(FlourishingEffects.NIRVANA.get(), 6000));
            }
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
                vegeance = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getMainHandItem()) > 0;
                grave_digger += EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.GRAVE_DIGGER.get(),((LivingEntity) cause).getMainHandItem());
            }
            else if (direct instanceof ThrownTrident){
                    vegeance = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.VENGEANCE.get(), ((ThrownTrident) direct).getPickupItemStackOrigin()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.GRAVE_DIGGER.get(), ((ThrownTrident) direct).getPickupItemStackOrigin());
            }
            else if (direct instanceof AbstractArrow){
                if(((LivingEntity) cause).getMainHandItem().getItem() instanceof ProjectileWeaponItem){
                    vegeance= EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getMainHandItem()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.GRAVE_DIGGER.get(), ((LivingEntity) cause).getMainHandItem());
                }
                else if (((LivingEntity) cause).getOffhandItem().getItem() instanceof ProjectileWeaponItem){
                    vegeance= EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.VENGEANCE.get(), ((LivingEntity) cause).getOffhandItem()) > 0;
                    grave_digger += EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.GRAVE_DIGGER.get(), ((LivingEntity) cause).getOffhandItem());
                }
            }
            //仇决
            if(vegeance){
                if(((LivingEntity) cause).hasEffect(FlourishingEffects.REPULSE_THE_ENEMY.get()))
                    ((LivingEntity) cause).removeEffect(FlourishingEffects.REPULSE_THE_ENEMY.get());
                ((LivingEntity) cause).addEffect(new MobEffectInstance(FlourishingEffects.VENGEANCE.get(), 600 ,((LivingEntity) cause).hasEffect(FlourishingEffects.VENGEANCE.get()) ? ((LivingEntity) cause).getEffect(FlourishingEffects.VENGEANCE.get()).getAmplifier() + 1  : 0));
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