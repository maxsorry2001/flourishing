package net.Gmaj7.flourishing.eventdispose;

import com.google.common.collect.Maps;
import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.Init.DataInit;
import net.Gmaj7.flourishing.flourishingDamageType.FlourishingDamageTypes;
import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffects;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class ClickDispose {
    @SubscribeEvent
    public static void RightClickItem(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack itemStackChest = player.getItemBySlot(EquipmentSlot.CHEST);
        int cabFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.CHECK_AND_BALANCE.get(), itemStackChest);
        if(!player.level().isClientSide()){
            if (cabFlag > 0
                && player.isShiftKeyDown()
                && !player.hasEffect(FlourishingEffects.CHECK_AND_BALANCE.get())){
                int count =  player.getItemInHand(event.getHand()).getCount() + (cabFlag == 2 ? player.getItemInHand(event.getHand()).getCount() / 16 * 4 :0);
                if(player.getItemInHand(event.getHand()).getMaxStackSize() == 1 && cabFlag == 2) count += 1;
                if(player.getItemInHand(event.getHand()).getItem() instanceof ArrowItem){
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    for (int i = 0; i < count; i++){
                        boolean flag = new Random().nextBoolean();
                        ItemStack itemStack;
                        if(flag) itemStack = new ItemStack(Items.ARROW);
                        else{
                            itemStack = new ItemStack(Items.TIPPED_ARROW);
                            PotionUtils.setPotion(itemStack, DataInit.potions[new Random().nextInt(DataInit.potions.length)]);
                        }
                        ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                        player.level().addFreshEntity(itemEntity);
                    }
                }
                else if(player.getItemInHand(event.getHand()).getItem() instanceof PotionItem){
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    for (int i = 0; i < count; i++){
                        ItemStack itemStack = null;
                        switch (new Random().nextInt(3)){
                            case 0 -> itemStack = new ItemStack(Items.POTION);
                            case 1 -> itemStack = new ItemStack(Items.SPLASH_POTION);
                            case 2 -> itemStack = new ItemStack(Items.LINGERING_POTION);
                        }
                        PotionUtils.setPotion(itemStack, DataInit.potions[new Random().nextInt(DataInit.potions.length)]);
                        ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                        player.level().addFreshEntity(itemEntity);
                    }
                }
                else if(new DataInit().isEquipment(player.getItemInHand(event.getHand()))){
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    for (int i = 0; i < count; i++){
                        ItemStack itemStack = DataInit.itemStackEquip[new Random().nextInt(DataInit.itemStackEquip.length)];
                        ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                        player.level().addFreshEntity(itemEntity);
                    }
                }
                else if(player.getItemInHand(event.getHand()).isEdible()){
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    if(player.getItemInHand(event.getHand()).getMaxStackSize() == 1)
                        count += 1;
                    for (int i = 0; i < count; i++){
                        ItemStack itemStack = DataInit.foods[new Random().nextInt(DataInit.foods.length)];
                        if(itemStack.getItem() == Items.SUSPICIOUS_STEW){
                            List<SuspiciousEffectHolder> list = SuspiciousEffectHolder.getAllEffectHolders();
                            SuspiciousStewItem.saveMobEffects(itemStack, list.get(new Random().nextInt(list.size())).getSuspiciousEffects());
                        }
                        ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                        player.level().addFreshEntity(itemEntity);
                    }
                }
                player.addEffect(new MobEffectInstance(FlourishingEffects.CHECK_AND_BALANCE.get(), 600, 0));
                event.setResult(Event.Result.DENY);
            }
            for (int i = 0; i < player.getInventory().getContainerSize(); i++){
                if (EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.PRECISION.get(), player.getInventory().getItem(i)) == 3){
                    if(player.getItemInHand(event.getHand()).getItem() instanceof ThrowablePotionItem){
                        if(!player.getInventory().getItem(i).hasTag()){
                            CompoundTag nbtData = new CompoundTag();
                            nbtData.putBoolean("flourishing.precision_potion", true);
                            player.getInventory().getItem(i).setTag(nbtData);
                        }
                        else if(!player.getInventory().getItem(i).getTag().contains("flourishing.precision_potion"))
                            player.getInventory().getItem(i).getTag().putBoolean("flourishing.precision_potion", true);
                    }
                    if(player.getItemInHand(event.getHand()).getItem() instanceof ArmorItem){
                        if(!player.getInventory().getItem(i).hasTag()){
                            CompoundTag nbtData = new CompoundTag();
                            nbtData.putBoolean("flourishing.precision_armor", true);
                            player.getInventory().getItem(i).setTag(nbtData);
                        }
                        else if(!player.getInventory().getItem(i).getTag().contains("flourishing.precision_armor"))
                            player.getInventory().getItem(i).getTag().putBoolean("flourishing.precision_armor", true);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void BlockPlace(BlockEvent.EntityPlaceEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            for (int i = 0; i < ((Player) entity).getInventory().getContainerSize(); i++){
                if (EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.PRECISION.get(), ((Player) entity).getInventory().getItem(i)) == 3){
                    if(!((Player) entity).getInventory().getItem(i).hasTag()){
                        CompoundTag nbtData = new CompoundTag();
                        nbtData.putBoolean("flourishing.precision_block", true);
                        ((Player) entity).getInventory().getItem(i).setTag(nbtData);
                    }
                    else if(!((Player) entity).getInventory().getItem(i).getTag().contains("flourishing.precision_block"))
                        ((Player) entity).getInventory().getItem(i).getTag().putBoolean("flourishing.precision_block", true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void EntityDeal(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Level level = player.level();
        Entity target = event.getTarget();
        ItemStack itemStack = player.getItemInHand(event.getHand());
        int powFlag = 0/*灭计*/;
        boolean seeClearlyFlag = false/*明鉴*/;
        seeClearlyFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.SEE_CLEARLY.get(), itemStack) > 0;
        powFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.PLAN_OF_WIPE.get(), itemStack);
        //灭计
        if (powFlag > 0 && target instanceof LivingEntity){
            int count = 0;
            for(InteractionHand hand : InteractionHand.values()){
                if(!((LivingEntity) target).getItemInHand(hand).isEmpty()){
                    count += 1;
                }
            }
            if(count > 0){
                itemStack.hurtAndBreak(10, player ,p -> p.broadcastBreakEvent(event.getHand()));
                int i = new Random().nextInt(2);
                if(((LivingEntity) target).getItemBySlot(DataInit.equipmentSlots[i]).isEmpty()) i = (i + 1) % 2;
                switch (powFlag){
                    case 1 -> {
                        target.setItemSlot(DataInit.equipmentSlots[i], ItemStack.EMPTY);
                        uniquePlanDeal(player, (LivingEntity) target);
                    }
                    case 2 -> {
                        ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), ((LivingEntity) target).getItemBySlot(DataInit.equipmentSlots[i]));
                        player.level().addFreshEntity(itemEntity);
                        target.setItemSlot(DataInit.equipmentSlots[i], ItemStack.EMPTY);
                        uniquePlanDeal(player, (LivingEntity) target);
                    }
                }
                player.swing(event.getHand());
            }
        }
        //明鉴
        if (seeClearlyFlag && target instanceof Monster) {
            if(!((Monster) target).getMainHandItem().isEmpty()) ((Monster) target).setItemInHand(InteractionHand.OFF_HAND,((Monster) target).getMainHandItem());
            ((Monster) target).setItemInHand(InteractionHand.MAIN_HAND, itemStack);
            player.setItemInHand(event.getHand(), ItemStack.EMPTY);
            ((Monster) target).setTarget(null);
            List<Monster> list = level.getEntitiesOfClass(Monster.class, new AABB(target.getX() -20, target.getY() - 3, target.getZ() - 20, target.getX() + 20, target.getY() + 3, target.getZ() + 20));
            for (Monster monster : list){
                if(monster == target){
                    continue;
                }
                ((Monster) target).setTarget(monster);
                break;
            }
            player.swing(event.getHand());
            ((Monster) target).targetSelector.removeGoal(new NearestAttackableTargetGoal<>((Mob) target, Player.class, true));
            ((Monster) target).targetSelector.addGoal( 1,new NearestAttackableTargetGoal<>((Mob) target, Monster.class, true));
            ((Monster) target).addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 0));
        }
    }

    public static void uniquePlanDeal(Player player, LivingEntity target){
        int lv = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.UNIQUE_PLAN.get(), player.getItemBySlot(EquipmentSlot.CHEST));
        int targetCount = 0;
        for (InteractionHand hand : InteractionHand.values()){
            if(!target.getItemInHand(hand).isEmpty()) targetCount++;
        }
        switch (lv){
            case 0 -> {}
            case 1 -> {
                if(targetCount == 0)
                    target.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), target.getMaxHealth() * 0.2F);
            }
            case 2 -> {

                target.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), target.getMaxHealth() * 0.35F);
            }
        }
    }
}