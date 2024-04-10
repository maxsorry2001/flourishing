package net.Gmaj7.interestingmod.modEnchantment;

import net.Gmaj7.interestingmod.InterestingMod;
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
    private static final Predicate<Item> trident = item -> item instanceof TridentItem;
    private static final Predicate<Item> sword = item -> item instanceof SwordItem;
    private static final Predicate<Item> bow = item -> item instanceof BowItem;
    private static final Predicate<Item> crossbow = item -> item instanceof CrossbowItem;
    private static final Predicate<Item> projectweapen = item -> item instanceof ProjectileWeaponItem;
    private static final Predicate<Item> tiered = item -> item instanceof TieredItem;

    public static Supplier<Enchantment> ARMYDESTROYER = ENCHANTMENTS.register("army_destroyer",
            () -> new ArmyDestroyer(Enchantment.Rarity.COMMON, EnchantmentCategory.create("army_destroyer_can_enchant",
                                trident.or(sword).or(projectweapen))));
    public static Supplier<Enchantment> COMPANY = ENCHANTMENTS.register("company",
            () -> new Company(Enchantment.Rarity.COMMON,EnchantmentCategory.create("company_enchant",tiered)));
    public static Supplier<Enchantment> ARRORGANT_AND_WILFUL = ENCHANTMENTS.register("arrogant_and_wilful",
            () -> new ArrogantAndWilful(Enchantment.Rarity.COMMON, EnchantmentCategory.create("aaw_can_enchant",
                                trident.or(sword).or(projectweapen))));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}
