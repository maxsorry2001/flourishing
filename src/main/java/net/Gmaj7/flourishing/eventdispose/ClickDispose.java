package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffects;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class ClickDispose {
    @SubscribeEvent
    public static void RightClick(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack itemStackChest = player.getItemBySlot(EquipmentSlot.CHEST);
        int cabFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.CHECK_AND_BALANCE.get(), itemStackChest);
        if(!player.level().isClientSide()
                && cabFlag > 0
                && player.isShiftKeyDown()
                && !player.hasEffect(FlourishingEffects.CHECK_AND_BALANCE.get())){
            int count =  player.getItemInHand(event.getHand()).getCount() + (cabFlag == 2 ? player.getItemInHand(event.getHand()).getCount() / 16 * 4 :0);
            if(player.getItemInHand(event.getHand()).getItem() instanceof ArrowItem){
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                for (int i = 0; i < count; i++){
                    boolean flag = new Random().nextBoolean();
                    ItemStack itemStack;
                    if(flag) itemStack = new ItemStack(Items.ARROW);
                    else{
                        itemStack = new ItemStack(Items.TIPPED_ARROW);
                        PotionUtils.setPotion(itemStack, LootPool.potions[new Random().nextInt(LootPool.potions.length)]);
                    }
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                    player.level().addFreshEntity(itemEntity);
                }
            }
            else if(player.getItemInHand(event.getHand()).getItem() instanceof PotionItem){
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                for (int i = 0; i < count + cabFlag - 1; i++){
                    ItemStack itemStack = null;
                    switch (new Random().nextInt(3)){
                        case 0 -> itemStack = new ItemStack(Items.POTION);
                        case 1 -> itemStack = new ItemStack(Items.SPLASH_POTION);
                        case 2 -> itemStack = new ItemStack(Items.LINGERING_POTION);
                    }
                    PotionUtils.setPotion(itemStack, LootPool.potions[new Random().nextInt(LootPool.potions.length)]);
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                    player.level().addFreshEntity(itemEntity);
                }
            }
            else if(new LootPool().isEquipment(player.getItemInHand(event.getHand()))){
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                for (int i = 0; i < count; i++){
                    ItemStack itemStack = LootPool.itemStackEquip[new Random().nextInt(LootPool.itemStackEquip.length)];
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                    player.level().addFreshEntity(itemEntity);
                }
            }
            else if(player.getItemInHand(event.getHand()).isEdible()){
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                if(player.getItemInHand(event.getHand()).getMaxStackSize() == 1)
                    count += 1;
                for (int i = 0; i < count; i++){
                    ItemStack itemStack = LootPool.foods[new Random().nextInt(LootPool.foods.length)];
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
    }

    @SubscribeEvent
    public static void EntityDeal(PlayerInteractEvent.EntityInteract interact){
        Player player = interact.getEntity();
        Level level = player.level();
        Entity target = interact.getTarget();
        ItemStack itemStack = player.getItemInHand(interact.getHand());
        if(!level.isClientSide()
                && EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.SEE_CLEARLY.get(), itemStack) > 0
                && target instanceof Monster) {
            if(!((Monster) target).getMainHandItem().isEmpty()) ((Monster) target).setItemInHand(InteractionHand.OFF_HAND,((Monster) target).getMainHandItem());
            ((Monster) target).setItemInHand(InteractionHand.MAIN_HAND, itemStack);
            player.setItemInHand(interact.getHand(), ItemStack.EMPTY);
            List<Monster> list = level.getEntitiesOfClass(Monster.class, new AABB(target.getX() -20, target.getY() - 3, target.getZ() - 20, target.getX() + 20, target.getY() + 3, target.getZ() + 20));
            for (Monster monster : list){
                if(monster == target){
                    ((Monster) target).setTarget(null);
                    continue;
                }
                ((Monster) target).setTarget(monster);
                break;
            }
            player.swing(interact.getHand(), true);
            ((Monster) target).targetSelector.removeGoal(new NearestAttackableTargetGoal<>((Mob) target, Player.class, true));
            ((Monster) target).targetSelector.addGoal( 1,new NearestAttackableTargetGoal<>((Mob) target, Monster.class, true));
            ((Monster) target).addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 0));
        }
    }
}