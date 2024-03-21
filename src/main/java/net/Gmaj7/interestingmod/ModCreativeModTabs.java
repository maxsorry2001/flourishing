package net.Gmaj7.interestingmod;

import net.Gmaj7.interestingmod.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, InterestingMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LEGENDS_OF_THE_THREE_KINGDOMS_TAB = CREATIVE_MODE_TABS.register("legends_of_the_three_kingdoms",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.legends_of_the_three_kingdoms"))
                    .icon(() -> ModItems.ANCIENT_INGOT_KNIFE.get().getDefaultInstance())
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.ANCIENT_INGOT_KNIFE.get());
                        pOutput.accept(ModItems.WINE_JARS.get());
                        pOutput.accept(ModItems.WINE.get());
                    }))
                    .build());
}
