package com.example.interestingmod;

import com.example.interestingmod.items.potion.ModPotions;
import com.example.interestingmod.modeffect.ModEffects;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static com.example.interestingmod.items.ModItems.ITEMS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(InterestingMod.MODID)
public class InterestingMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "interestingmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public InterestingMod(IEventBus modEventBus)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ITEMS.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }
    @SubscribeEvent
    private void lava_potion_get(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        Level level = player.level();
        if(!level.isClientSide() && event.getHand() == InteractionHand.MAIN_HAND){
            Item item1 = player.getMainHandItem().getItem();
            Item item2 = player.getOffhandItem().getItem();
            if((item1 == Items.LAVA_BUCKET && item2 == Items.GLASS_BOTTLE) ||
                    (item1 == Items.GLASS_BOTTLE && item2 == Items.LAVA_BUCKET)){
                ItemStack itemStack1 = new ItemStack(Items.POTION);
                ItemStack itemStack2 = new ItemStack(Items.BUCKET);
                PotionUtils.setPotion(itemStack1 , ModPotions.LAVA.get());
                player.getInventory().removeItem(player.getMainHandItem());
                player.getInventory().removeItem(player.getOffhandItem());
                if(item1 == Items.GLASS_BOTTLE){
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack1);
                    player.setItemInHand(InteractionHand.OFF_HAND, itemStack2);
                }
                else {
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStack2);
                    player.setItemInHand(InteractionHand.OFF_HAND, itemStack1);
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS);
            }
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}