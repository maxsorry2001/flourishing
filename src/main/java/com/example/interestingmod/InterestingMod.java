package com.example.interestingmod;

import com.example.interestingmod.items.potion.ModPotions;
import com.example.interestingmod.modeffect.ModEffects;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
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
            ItemStack itemStackMain = player.getMainHandItem();
            ItemStack itemStackOff = player.getOffhandItem();
            Item itemMain = itemStackMain.getItem();
            Item itemOff = itemStackOff.getItem();
            if((itemMain == Items.LAVA_BUCKET && itemOff == Items.GLASS_BOTTLE) ||
                    (itemMain == Items.GLASS_BOTTLE && itemOff == Items.LAVA_BUCKET)){
                ItemStack itemStackPotion = new ItemStack(Items.POTION);
                ItemStack itemStackBucket = new ItemStack(Items.BUCKET);
                PotionUtils.setPotion(itemStackPotion , ModPotions.LAVA.get());
                if(itemMain == Items.GLASS_BOTTLE){
                    if(itemStackMain.getCount() > 1){
                        itemStackMain.setCount(itemStackMain.getCount() - 1);
                        player.getInventory().removeItem(itemStackMain);
                        player.getInventory().removeItem(itemStackOff);
                        player.setItemInHand(InteractionHand.MAIN_HAND, itemStackMain);
                        player.setItemInHand(InteractionHand.OFF_HAND, itemStackBucket);
                        player.addItem(itemStackPotion);
                    }
                    else {
                        player.getInventory().removeItem(itemStackMain);
                        player.getInventory().removeItem(itemStackOff);
                        player.setItemInHand(InteractionHand.MAIN_HAND,itemStackPotion);
                        player.setItemInHand(InteractionHand.OFF_HAND,itemStackBucket);
                    }
                }
                else {
                    if(itemStackOff.getCount() > 1){
                        itemStackOff.setCount(itemStackOff.getCount() - 1);
                        player.getInventory().removeItem(itemStackMain);
                        player.getInventory().removeItem(itemStackOff);
                        player.setItemInHand(InteractionHand.OFF_HAND, itemStackOff);
                        player.setItemInHand(InteractionHand.MAIN_HAND, itemStackBucket);
                        player.addItem(itemStackPotion);
                    }
                    else {
                        player.getInventory().removeItem(itemStackMain);
                        player.getInventory().removeItem(itemStackOff);
                        player.setItemInHand(InteractionHand.OFF_HAND,itemStackPotion);
                        player.setItemInHand(InteractionHand.MAIN_HAND,itemStackBucket);
                    }
                }
            }
        }
        if(level.isClientSide() && event.getHand() == InteractionHand.MAIN_HAND
                && ((player.getMainHandItem().getItem() == Items.GLASS_BOTTLE && player.getOffhandItem().getItem() == Items.LAVA_BUCKET)
                || (player.getMainHandItem().getItem() == Items.LAVA_BUCKET && player.getOffhandItem().getItem() == Items.GLASS_BOTTLE))){
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS);
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