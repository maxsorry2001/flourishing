package com.example.interestingmod.items;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.example.interestingmod.InterestingMod.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final Supplier<Item> STEEL = ITEMS.register("steel",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SAFETY_HELMET = ITEMS.register("saftey_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET, new Item.Properties()));
}
