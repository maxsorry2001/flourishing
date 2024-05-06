package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.Init.DataInit;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class ItemDispose {
    @SubscribeEvent
    public static void ItemGet(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.SELF_WRITING.get(), player.getItemBySlot(EquipmentSlot.CHEST)) > 0) {
            boolean a = !event.getItem().getItem().hasTag();
            boolean b = !a && !event.getItem().getItem().getTag().contains("flourishing.self_write");
            CompoundTag nbtData = new CompoundTag();
            nbtData.putBoolean("flourishing.self_write", true);
            if (new DataInit().isEquipment(event.getItem().getItem())) {
                if (a || b) {
                    if (a) {
                        event.getItem().getItem().setTag(nbtData);
                    }
                    if (b) {
                        event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    }
                    event.getItem().getItem().setTag(nbtData);
                    ItemStack itemNew = DataInit.itemStackEquip[new Random().nextInt(DataInit.itemStackEquip.length)];
                    CompoundTag nbtDataNew = new CompoundTag();
                    nbtDataNew.putBoolean("flourishing.self_write", true);
                    itemNew.setTag(nbtDataNew);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            } else if (event.getItem().getItem().getItem().isEdible()) {
                if (a || b) {
                    if (a) {
                        event.getItem().getItem().setTag(nbtData);
                    }
                    if (b) {
                        event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    }
                    ItemStack itemNew = DataInit.foods[new Random().nextInt(DataInit.foods.length)];
                    if(itemNew.getItem() == Items.SUSPICIOUS_STEW){
                        List<SuspiciousEffectHolder> list = SuspiciousEffectHolder.getAllEffectHolders();
                        SuspiciousStewItem.saveMobEffects(itemNew, list.get(new Random().nextInt(list.size())).getSuspiciousEffects());
                    }
                    if(!itemNew.hasTag()) itemNew.setTag(nbtData);
                    else itemNew.getTag().putBoolean("flourishing.self_write", true);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            } else if (event.getItem().getItem().getItem() instanceof PotionItem) {
                if (b) {
                    event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    ItemStack itemNew = null;
                    switch (new Random().nextInt(3)) {
                        case 0 -> {
                            itemNew = new ItemStack(Items.POTION);
                        }
                        case 1 -> {
                            itemNew = new ItemStack(Items.LINGERING_POTION);
                        }
                        case 2 -> {
                            itemNew = new ItemStack(Items.SPLASH_POTION);
                        }
                    }
                    PotionUtils.setPotion(itemNew, DataInit.potions[new Random().nextInt(DataInit.potions.length)]);
                    itemNew.getTag().putBoolean("flourishing.self_write", true);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            } else if (event.getItem().getItem().getItem() instanceof ArrowItem) {
                if (a || b) {
                    if (a) {
                        event.getItem().getItem().setTag(nbtData);
                    }
                    if (b) {
                        event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    }
                    boolean flag = new Random().nextBoolean();
                    ItemStack itemStackNew;
                    if (flag) {
                        itemStackNew = new ItemStack(Items.ARROW);
                        itemStackNew.setTag(nbtData);
                    }
                    else {
                        itemStackNew = new ItemStack(Items.TIPPED_ARROW);
                        PotionUtils.setPotion(itemStackNew, DataInit.potions[new Random().nextInt(DataInit.potions.length)]);
                        itemStackNew.getTag().putBoolean("flourishing.self_write", true);
                    }
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemStackNew);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void ItemUse (LivingEntityUseItemEvent.Finish event){
        LivingEntity livingEntity = event.getEntity();
        if(livingEntity instanceof Player){
            for (int i = 0; i < ((Player) livingEntity).getInventory().getContainerSize(); i++){
                if (EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.PRECISION.get(), ((Player) livingEntity).getInventory().getItem(i)) == 3){
                    if(event.getItem().getItem().isEdible()){
                        if(!((Player) livingEntity).getInventory().getItem(i).hasTag()){
                            CompoundTag nbtData = new CompoundTag();
                            nbtData.putBoolean("flourishing.precision_food", true);
                            ((Player) livingEntity).getInventory().getItem(i).setTag(nbtData);
                        }
                        else if(!((Player) livingEntity).getInventory().getItem(i).getTag().contains("flourishing.precision_food"))
                            ((Player) livingEntity).getInventory().getItem(i).getTag().putBoolean("flourishing.precision_food", true);
                    }
                    if(event.getItem().getItem() instanceof PotionItem && !(event.getItem().getItem() instanceof ThrowablePotionItem)){
                        if(!((Player) livingEntity).getInventory().getItem(i).hasTag()){
                            CompoundTag nbtData = new CompoundTag();
                            nbtData.putBoolean("flourishing.precision_potion", true);
                            ((Player) livingEntity).getInventory().getItem(i).setTag(nbtData);
                        }
                        else if(!((Player) livingEntity).getInventory().getItem(i).getTag().contains("flourishing.precision_potion"))
                            ((Player) livingEntity).getInventory().getItem(i).getTag().putBoolean("flourishing.precision_potion", true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void company(PlayerDestroyItemEvent event){
        ItemStack itemStack = event.getOriginal();
        Player player = event.getEntity();
        if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.COMPANY.get(), itemStack) > 0){
            Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
            enchantmentIntegerMap.put(FlourishingEnchantments.COMPANY.get(), 1);
            ItemStack itemStackNew = DataInit.itemStackEquip[new Random().nextInt(30)];
            itemStackNew.setDamageValue(itemStackNew.getMaxDamage() - 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, itemStackNew);
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStackNew);
            player.level().addFreshEntity(itemEntity);
        }
    }

    @SubscribeEvent
    public static void toolTip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            if (tag.contains("flourishing.self_write") && tag.getBoolean("flourishing.self_write")) {
                event.getToolTip().add(Component.translatable("has_been_got_self_write"));
            }
            if (tag.contains("flourishing.precision_food") && tag.getBoolean("flourishing.precision_food")) {
                event.getToolTip().add(Component.translatable("precision_food"));
            }
            if (tag.contains("flourishing.precision_block") && tag.getBoolean("flourishing.precision_block")) {
                event.getToolTip().add(Component.translatable("precision_block"));
            }
            if (tag.contains("flourishing.precision_armor") && tag.getBoolean("flourishing.precision_armor")) {
                event.getToolTip().add(Component.translatable("precision_armor"));
            }
            if (tag.contains("flourishing.precision_potion") && tag.getBoolean("flourishing.precision_potion")) {
                event.getToolTip().add(Component.translatable("precision_potion"));
            }
        }
    }
}