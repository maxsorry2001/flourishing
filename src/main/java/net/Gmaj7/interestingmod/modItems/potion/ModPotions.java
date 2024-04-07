package net.Gmaj7.interestingmod.modItems.potion;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.modeffect.ModEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(Registries.POTION, InterestingMod.MODID);
    public static final Supplier<Potion> LAVA = POTIONS.register("lava_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.LAVA_BODY.get(), 9600)));
    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);}
}
