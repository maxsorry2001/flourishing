package net.Gmaj7.interestingmod.enchantment;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.items.weapon.AncientIngotKnife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS
            = DeferredRegister.create(Registries.ENCHANTMENT, InterestingMod.MODID);
    private static final Predicate<Item> predicate1 = item -> item instanceof TridentItem;
    private static final Predicate<Item> predicate2 = item -> item instanceof SwordItem;
    private static final Predicate<Item> predicate3 = item -> item instanceof BowItem;
    private static final Predicate<Item> predicate4 = item -> item instanceof CrossbowItem;

    public static Supplier<Enchantment> ARMYDESTROYER = ENCHANTMENTS.register("army_destroyer",
        () -> new ArmyDestroyer(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.create("army_destroyer_can_enchant",
                                predicate1.or(predicate2).or(predicate3).or(predicate4))));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}
