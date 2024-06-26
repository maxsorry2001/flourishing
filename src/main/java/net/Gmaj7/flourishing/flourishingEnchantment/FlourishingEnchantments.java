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
    private static final Predicate<Item> project_weapon = item -> item instanceof ProjectileWeaponItem;
    private static final Predicate<Item> tiered = item -> item instanceof TieredItem;

    public static Supplier<Enchantment> ARMY_DESTROYER = ENCHANTMENTS.register("army_destroyer",
            () -> new ArmyDestroyer(Enchantment.Rarity.COMMON, EnchantmentCategory.create("army_destroyer_can_enchant",
                                trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> COMPANY = ENCHANTMENTS.register("company",
            () -> new Company(Enchantment.Rarity.COMMON,EnchantmentCategory.create("company_enchant",tiered)));
    public static Supplier<Enchantment> ARROGANT_AND_WILFUL = ENCHANTMENTS.register("arrogant_and_wilful",
            () -> new ArrogantAndWilful(Enchantment.Rarity.COMMON, EnchantmentCategory.create("aaw_can_enchant",
                                trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> REPULSE_THE_ENEMY = ENCHANTMENTS.register("repulse_the_enemy",
            () -> new RepulseTheEnemy(Enchantment.Rarity.COMMON, EnchantmentCategory.create("rte_can_enchant",
                                trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> VENGEANCE = ENCHANTMENTS.register("vengeance",
            () -> new RepulseTheEnemy(Enchantment.Rarity.COMMON, EnchantmentCategory.create("vengeance_can_enchant",
                    trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> CHECK_AND_BALANCE = ENCHANTMENTS.register("check_and_balance",
            () -> new CheckAndBalance(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST));
    public static Supplier<Enchantment> GRAVE_DIGGER = ENCHANTMENTS.register("grave_digger",
            () -> new GraveDigger(Enchantment.Rarity.COMMON, EnchantmentCategory.create("grave_digger_can_enchant",
                    trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> BANISH = ENCHANTMENTS.register("banish",
            () -> new Banish(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST));
    public static Supplier<Enchantment> ROAR = ENCHANTMENTS.register("roar",
            () -> new Roar(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON));
    public static Supplier<Enchantment> UNFEELING = ENCHANTMENTS.register("unfeeling",
            () -> new Unfelling(Enchantment.Rarity.COMMON,EnchantmentCategory.create("unfeeling",
                    trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> Nirvana = ENCHANTMENTS.register("nirvana",
            () -> new Nirvana(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD));
    public static Supplier<Enchantment> SEE_CLEARLY = ENCHANTMENTS.register("see_clearly",
            () -> new SeeClearly(Enchantment.Rarity.COMMON, EnchantmentCategory.create("see_clearly_can_enchantment",
                    trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> SELF_WRITING = ENCHANTMENTS.register("self_writing",
            () -> new SelfWriting(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST));
    public static Supplier<Enchantment> PRECISION = ENCHANTMENTS.register("precision",
            () -> new Precision(Enchantment.Rarity.COMMON, EnchantmentCategory.BOW));
    public static Supplier<Enchantment> PLAN_OF_WIPE = ENCHANTMENTS.register("plan_of_wipe",
            () -> new PlanOfWipe(Enchantment.Rarity.COMMON, EnchantmentCategory.create("plw_can_enchant",
                    trident.or(sword).or(project_weapon))));
    public static Supplier<Enchantment> UNIQUE_PLAN = ENCHANTMENTS.register("unique_plan",
            () -> new UniquePlan(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_CHEST));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}