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
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class ClickDispose {
    static Potion[] potions = {Potions.NIGHT_VISION, Potions.FIRE_RESISTANCE, Potions.HARMING, Potions.HEALING, Potions.INVISIBILITY,
            Potions.LEAPING, Potions.LUCK, Potions.REGENERATION, Potions.SLOWNESS, Potions.STRENGTH,
            Potions.SLOW_FALLING, Potions.SWIFTNESS, Potions.TURTLE_MASTER, Potions.WATER_BREATHING, Potions.WEAKNESS,
            Potions.LONG_FIRE_RESISTANCE, Potions.LONG_INVISIBILITY, Potions.LONG_LEAPING, Potions.LONG_REGENERATION,
            Potions.LONG_NIGHT_VISION, Potions.LONG_SLOW_FALLING, Potions.LONG_SLOWNESS, Potions.LONG_STRENGTH,
            Potions.LONG_SWIFTNESS, Potions.LONG_TURTLE_MASTER, Potions.LONG_WATER_BREATHING, Potions.LONG_WEAKNESS,
            Potions.STRONG_HARMING, Potions.STRONG_HEALING, Potions.STRONG_LEAPING, Potions.STRONG_REGENERATION, Potions.STRONG_SLOWNESS,
            Potions.STRONG_STRENGTH, Potions.STRONG_SWIFTNESS, Potions.STRONG_TURTLE_MASTER};
    @SubscribeEvent
    public static void RightClick(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack itemStackChest = player.getItemBySlot(EquipmentSlot.CHEST);
        int cabFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.CHECK_AND_BALANCE.get(), itemStackChest);
        if(!player.level().isClientSide()
                && cabFlag > 0
                && player.isShiftKeyDown()
                && !player.hasEffect(FlourishingEffects.CHECK_AND_BALANCE.get())){
            if(player.getItemInHand(event.getHand()).getItem() instanceof ArrowItem){
                for (int i = 0; i < player.getItemInHand(event.getHand()).getCount() + (cabFlag == 2 ? player.getItemInHand(event.getHand()).getCount() / 16 * 4 :0); i++){
                    ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW);
                    PotionUtils.setPotion(itemStack, potions[new Random().nextInt(potions.length)]);
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    player.level().addFreshEntity(itemEntity);
                }
            }
            else if(player.getItemInHand(event.getHand()).getItem() instanceof PotionItem){
                for (int i = 0; i < player.getItemInHand(event.getHand()).getCount() + (cabFlag == 2 ? player.getItemInHand(event.getHand()).getCount() / 16 * 4 :0); i++){
                    ItemStack itemStack = new ItemStack(Items.POTION);
                    PotionUtils.setPotion(itemStack, potions[new Random().nextInt(potions.length)]);
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                    player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                    player.level().addFreshEntity(itemEntity);
                }
            }
            player.addEffect(new MobEffectInstance(FlourishingEffects.CHECK_AND_BALANCE.get(), 1200, 0));
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