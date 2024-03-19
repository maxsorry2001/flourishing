package net.Gmaj7.interestingmod.modeffect;

import net.Gmaj7.interestingmod.InterestingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT
            = DeferredRegister.create(Registries.MOB_EFFECT, InterestingMod.MODID);

    public static final Supplier<MobEffect> LAVA_BODY = MOB_EFFECT.register("lava_body",
            () -> new LavaBody(MobEffectCategory.BENEFICIAL, 11367763));

    public static void register(IEventBus eventBus){MOB_EFFECT.register(eventBus);}
}
