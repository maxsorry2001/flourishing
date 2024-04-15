package net.Gmaj7.flourishing.modeffect;

import net.Gmaj7.flourishing.Flourishing;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT
            = DeferredRegister.create(Registries.MOB_EFFECT, Flourishing.MODID);

    public static final Supplier<MobEffect> REPULSE_THE_ENEMY = MOB_EFFECT.register("repulse_the_enemy",
            () -> new ModEffect(MobEffectCategory.NEUTRAL, 11982273));
    public static final Supplier<MobEffect> BACKWATER = MOB_EFFECT.register("backwater",
            () -> new ModEffect(MobEffectCategory.NEUTRAL,11112298)
                    .addAttributeModifier(Attributes.MAX_HEALTH,"99AD56AC-DD61-CDCA-AF87-72819CDCA7D1", -2, AttributeModifier.Operation.ADDITION));
    public static final Supplier<MobEffect> VENGEANCE = MOB_EFFECT.register("vengeance",
            () -> new ModEffect(MobEffectCategory.BENEFICIAL, 65321109)
                    .addAttributeModifier(Attributes.MAX_HEALTH, "99AD56AC-DD61-CDCA-AF87-72819CDCA7D2", 2, AttributeModifier.Operation.ADDITION));
    public static final Supplier<MobEffect> BANISHMENT = MOB_EFFECT.register("banishment",
            () -> new ModEffect(MobEffectCategory.HARMFUL, 14736286)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "99AD56AC-DD61-CDCA-AF87-72819CDCA7D3", -0.999F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final Supplier<MobEffect> CHECK_AND_BALANCE = MOB_EFFECT.register("check_and_balance",
            () -> new ModEffect(MobEffectCategory.NEUTRAL, 30098213));

    public static void register(IEventBus eventBus){MOB_EFFECT.register(eventBus);}
}