package net.Gmaj7.flourishing.flourishingEnchantment;

import net.Gmaj7.flourishing.Flourishing;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FlourishingEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS
            = DeferredRegister.create(Registries.ENCHANTMENT, Flourishing.MODID);
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
    public static Supplier<Enchantment> REPULSE_THE_ENEMY = ENCHANTMENTS.register("repulse_the_enemy",
            () -> new RepulseTheEnemy(Enchantment.Rarity.COMMON, EnchantmentCategory.create("rte_can_enchant",
                                trident.or(sword).or(projectweapen))));
    public static Supplier<Enchantment> VENGEANCE = ENCHANTMENTS.register("vengeance",
            () -> new RepulseTheEnemy(Enchantment.Rarity.COMMON, EnchantmentCategory.create("vengeance_can_enchant",
                    trident.or(sword).or(projectweapen))));
    public static Supplier<Enchantment> CHECK_AND_BALABCE = ENCHANTMENTS.register("check_and_balance",
            () -> new CheckAndBalance(Enchantment.Rarity.COMMON, EnchantmentCategory.create("cab_can_enchant",
                    projectweapen)));
    public static Supplier<Enchantment> GRAVE_DIGGER = ENCHANTMENTS.register("grave_digger",
            () -> new GraveDigger(Enchantment.Rarity.COMMON, EnchantmentCategory.create("grave_digger",
                    trident.or(sword).or(projectweapen))));
    public static Supplier<Enchantment> BANISH = ENCHANTMENTS.register("banish",
            () -> new Banish(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST));
    public static Supplier<Enchantment> ROAR = ENCHANTMENTS.register("roar",
            () -> new Roar(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON));
    public static Supplier<Enchantment> UNFEELING = ENCHANTMENTS.register("unfeeling",
            () -> new Unfelling(Enchantment.Rarity.COMMON,EnchantmentCategory.create("unfeeling",
                    trident.or(sword).or(projectweapen))));
    public static Supplier<Enchantment> Nirvana = ENCHANTMENTS.register("nirvana",
            () -> new Nirvana(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}