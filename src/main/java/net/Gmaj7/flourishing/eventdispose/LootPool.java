package net.Gmaj7.flourishing.eventdispose;

import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class LootPool {
    static ItemStack[] itemStackEquip = {new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.STONE_SHOVEL),
            new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.GOLDEN_SHOVEL),
            new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.NETHERITE_SHOVEL),
            new ItemStack(Items.WOODEN_PICKAXE), new ItemStack(Items.STONE_PICKAXE),
            new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.GOLDEN_PICKAXE),
            new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.NETHERITE_PICKAXE),
            new ItemStack(Items.WOODEN_AXE), new ItemStack(Items.STONE_AXE),
            new ItemStack(Items.IRON_AXE), new ItemStack(Items.GOLDEN_AXE),
            new ItemStack(Items.DIAMOND_AXE), new ItemStack(Items.NETHERITE_AXE),
            new ItemStack(Items.WOODEN_SWORD), new ItemStack(Items.STONE_SWORD),
            new ItemStack(Items.IRON_SWORD), new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.WOODEN_HOE), new ItemStack(Items.STONE_HOE),
            new ItemStack(Items.IRON_HOE), new ItemStack(Items.GOLDEN_HOE),
            new ItemStack(Items.DIAMOND_HOE), new ItemStack(Items.NETHERITE_HOE),
            new ItemStack(Items.BOW), new ItemStack(Items.CROSSBOW), new ItemStack(Items.TRIDENT),
            new ItemStack(Items.SHEARS), new ItemStack(Items.FLINT_AND_STEEL),
            new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.CHAINMAIL_HELMET), new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.IRON_HELMET), new ItemStack(Items.GOLDEN_HELMET), new ItemStack(Items.NETHERITE_HELMET),
            new ItemStack(Items.TURTLE_HELMET), new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(Items.CHAINMAIL_CHESTPLATE),
            new ItemStack(Items.DIAMOND_CHESTPLATE), new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Items.IRON_CHESTPLATE),
            new ItemStack(Items.NETHERITE_CHESTPLATE), new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.CHAINMAIL_LEGGINGS),
            new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.NETHERITE_LEGGINGS),
            new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Items.CHAINMAIL_BOOTS),
            new ItemStack(Items.DIAMOND_BOOTS), new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Items.IRON_BOOTS),
            new ItemStack(Items.NETHERITE_BOOTS)};

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
            new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.RABBIT_STEW), new ItemStack(Items.HONEY_BOTTLE),
            new ItemStack(Items.SUSPICIOUS_STEW)};

    public boolean isEquipment(ItemStack itemStack){
        Item item = itemStack.getItem();
        return item instanceof ProjectileWeaponItem || item instanceof TieredItem
                || item instanceof TridentItem || item instanceof ArmorItem
                || item instanceof FlintAndSteelItem || item instanceof ShearsItem;
    }
}
