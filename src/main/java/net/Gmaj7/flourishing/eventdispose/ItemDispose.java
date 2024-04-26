package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class ItemDispose {
    static ItemStack[] itemStackTool ={new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.STONE_SHOVEL),
            new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.GOLDEN_SHOVEL),
            new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.NETHERITE_SHOVEL),
            new ItemStack(Items.WOODEN_PICKAXE), new ItemStack(Items.STONE_PICKAXE),
            new ItemStack(Items.IRON_PICKAXE),new ItemStack(Items.GOLDEN_PICKAXE),
            new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.NETHERITE_PICKAXE),
            new ItemStack(Items.WOODEN_AXE), new ItemStack(Items.STONE_AXE),
            new ItemStack(Items.IRON_AXE),new ItemStack(Items.GOLDEN_AXE),
            new ItemStack(Items.DIAMOND_AXE), new ItemStack(Items.NETHERITE_AXE),
            new ItemStack(Items.WOODEN_SWORD), new ItemStack(Items.STONE_SWORD),
            new ItemStack(Items.IRON_SWORD),new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.WOODEN_HOE), new ItemStack(Items.STONE_HOE),
            new ItemStack(Items.IRON_HOE),new ItemStack(Items.GOLDEN_HOE),
            new ItemStack(Items.DIAMOND_HOE), new ItemStack(Items.NETHERITE_HOE),
            new ItemStack(Items.BOW), new ItemStack(Items.CROSSBOW), new ItemStack(Items.TRIDENT)};

    static Potion[] potions = {Potions.NIGHT_VISION, Potions.FIRE_RESISTANCE, Potions.HARMING, Potions.HEALING, Potions.INVISIBILITY,
            Potions.LEAPING, Potions.LUCK, Potions.REGENERATION, Potions.SLOWNESS, Potions.STRENGTH,
            Potions.SLOW_FALLING, Potions.SWIFTNESS, Potions.TURTLE_MASTER, Potions.WATER_BREATHING, Potions.WEAKNESS,
            Potions.LONG_FIRE_RESISTANCE, Potions.LONG_INVISIBILITY, Potions.LONG_LEAPING, Potions.LONG_REGENERATION,
            Potions.LONG_NIGHT_VISION, Potions.LONG_SLOW_FALLING, Potions.LONG_SLOWNESS, Potions.LONG_STRENGTH,
            Potions.LONG_SWIFTNESS, Potions.LONG_TURTLE_MASTER, Potions.LONG_WATER_BREATHING, Potions.LONG_WEAKNESS,
            Potions.STRONG_HARMING, Potions.STRONG_HEALING, Potions.STRONG_LEAPING, Potions.STRONG_REGENERATION, Potions.STRONG_SLOWNESS,
            Potions.STRONG_STRENGTH, Potions.STRONG_SWIFTNESS, Potions.STRONG_TURTLE_MASTER};

    static ItemStack[] foods = {new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE),
            new ItemStack(Items.MELON_SLICE), new ItemStack(Items.SWEET_BERRIES), new ItemStack(Items.GLOW_BERRIES),
            new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.CARROT), new ItemStack(Items.GOLDEN_CARROT),
            new ItemStack(Items.POTATO), new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.POISONOUS_POTATO),
            new ItemStack(Items.BEETROOT), new ItemStack(Items.DRIED_KELP), new ItemStack(Items.BEEF),
            new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
            new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON), new ItemStack(Items.CHICKEN),
            new ItemStack(Items.COOKED_CHICKEN), new ItemStack(Items.RABBIT), new ItemStack(Items.COOKED_RABBIT),
            new ItemStack(Items.COD), new ItemStack(Items.COOKED_COD), new ItemStack(Items.SALMON), new ItemStack(Items.COOKED_SALMON),
            new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.BREAD), new ItemStack(Items.COOKIE),
            new ItemStack(Items.CAKE), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE),
            new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.RABBIT_STEW), new ItemStack(Items.HONEY_BOTTLE)};

    @SubscribeEvent
    public static void ItemGet(EntityItemPickupEvent event){
        Player player = event.getEntity();
        Level level = player.level();
        if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.SELF_WRITING.get(), player.getItemBySlot(EquipmentSlot.CHEST)) > 0){
            if(event.getItem().getItem().getItem() instanceof TieredItem || event.getItem().getItem().getItem() instanceof TridentItem
                    || event.getItem().getItem().getItem() instanceof ProjectileWeaponItem){
                if(!event.getItem().getItem().hasTag()){
                    CompoundTag nbtData = new CompoundTag();
                    nbtData.putBoolean("flourishing.self_write", true);
                    event.getItem().getItem().setTag(nbtData);
                    ItemStack itemNew = itemStackTool[new Random().nextInt(itemStackTool.length)];
                    CompoundTag nbtDataNew = new CompoundTag();
                    nbtDataNew.putBoolean("flourishing.self_write", true);
                    itemNew.setTag(nbtDataNew);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
                else if (!event.getItem().getItem().getTag().contains("flourishing.self_write")){
                    event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    ItemStack itemNew = itemStackTool[new Random().nextInt(itemStackTool.length)];
                    CompoundTag nbtData = new CompoundTag();
                    nbtData.putBoolean("flourishing.self_write", true);
                    itemNew.setTag(nbtData);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            }
            else if (event.getItem().getItem().getItem().isEdible()){
                if(!event.getItem().getItem().hasTag()){
                    CompoundTag nbtData = new CompoundTag();
                    nbtData.putBoolean("flourishing.self_write", true);
                    event.getItem().getItem().setTag(nbtData);
                    ItemStack itemNew = foods[new Random().nextInt(foods.length)];
                    CompoundTag nbtDataNew = new CompoundTag();
                    nbtDataNew.putBoolean("flourishing.self_write", true);
                    itemNew.setTag(nbtDataNew);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
                else if (!event.getItem().getItem().getTag().contains("flourishing.self_write")){
                    event.getItem().getItem().getTag().putBoolean("flourishing.self_write", true);
                    ItemStack itemNew = foods[new Random().nextInt(foods.length)];
                    CompoundTag nbtData = new CompoundTag();
                    nbtData.putBoolean("flourishing.self_write", true);
                    itemNew.setTag(nbtData);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            }
            else if (event.getItem().getItem().getItem() instanceof PotionItem){
                if (!event.getItem().getItem().getTag().contains("flourishing.self_write")){
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
                    PotionUtils.setPotion(itemNew, potions[new Random().nextInt(potions.length)]);
                    itemNew.getTag().putBoolean("flourishing.self_write", true);
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemNew);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void toolTip(ItemTooltipEvent event){
        ItemStack itemStack = event.getItemStack();
        if(itemStack.hasTag()){
            CompoundTag tag = itemStack.getTag();
            if(tag.contains("flourishing.self_write") && tag.getBoolean("flourishing.self_write")){
                event.getToolTip().add(Component.translatable("has_been_got_self_write"));
            }
        }
    }
}
