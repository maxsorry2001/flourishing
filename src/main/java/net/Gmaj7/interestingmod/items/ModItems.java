package net.Gmaj7.interestingmod.items;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.items.weapon.AncientIngotKnife;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(InterestingMod.MODID);
    public static final Supplier<Item> STEEL = ITEMS.register("steel",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SAFETY_HELMET = ITEMS.register("safety_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final Supplier<Item> HALF_LAVA_BUCKET = ITEMS.register("half_lava_bucket",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ANCIENT_INGOT_KNIFE = ITEMS.register("ancient_ingot_knife",
            () -> new AncientIngotKnife(new Item.Properties().durability(300)));
}