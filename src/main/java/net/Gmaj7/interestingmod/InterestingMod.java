package net.Gmaj7.interestingmod;

import com.mojang.logging.LogUtils;
import net.Gmaj7.interestingmod.modEnchantment.ModEnchantments;
import net.Gmaj7.interestingmod.modItems.ModItems;
import net.Gmaj7.interestingmod.modItems.potion.ModPotions;
import net.Gmaj7.interestingmod.modeffect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static net.Gmaj7.interestingmod.modItems.ModItems.ITEMS;

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
        ModEnchantments.register(modEventBus);

        ModCreativeModTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event){
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
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
            if(itemMain == Items.GLASS_BOTTLE && itemOff == Items.LAVA_BUCKET){
                ItemStack itemStackPotion = new ItemStack(Items.POTION);
                ItemStack itemStackBucket = new ItemStack(ModItems.HALF_LAVA_BUCKET.get());
                PotionUtils.setPotion(itemStackPotion , ModPotions.LAVA.get());
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
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStackPotion);
                    player.setItemInHand(InteractionHand.OFF_HAND, itemStackBucket);
                }
            }
            else if(itemMain == Items.GLASS_BOTTLE && itemOff == ModItems.HALF_LAVA_BUCKET.get()){
                ItemStack itemStackPotion = new ItemStack(Items.POTION);ItemStack itemStackBucket = new ItemStack(Items.BUCKET);PotionUtils.setPotion(itemStackPotion , ModPotions.LAVA.get());
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
                    player.setItemInHand(InteractionHand.MAIN_HAND, itemStackPotion);
                    player.setItemInHand(InteractionHand.OFF_HAND, itemStackBucket);
                }
            }
        }
        if(level.isClientSide() && event.getHand() == InteractionHand.MAIN_HAND
                && ((player.getMainHandItem().getItem() == Items.GLASS_BOTTLE
                && (player.getOffhandItem().getItem() == Items.LAVA_BUCKET
                    || player.getOffhandItem().getItem() == ModItems.HALF_LAVA_BUCKET.get()))))
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS);
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
    @Mod.EventBusSubscriber(modid = MODID)
    public static class FALL{
        private static boolean fall_safe = false;
        private static boolean sound_happen = false;
        @SubscribeEvent
        public static void test(LivingHurtEvent event) {
            LivingEntity livingEntity = event.getEntity();
            DamageSource damageSource = event.getSource();
            Level level = livingEntity.level();
            if (livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.SAFETY_HELMET.get() && livingEntity instanceof Player) {
                if (damageSource.is(DamageTypes.FLY_INTO_WALL)) {
                    int x = livingEntity.getBlockX();
                    int y = livingEntity.getBlockY();
                    int z = livingEntity.getBlockZ();
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            for (int k = -1; k < 2; k++) {
                                BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                                if (!level.getBlockState(blockPos).is(Blocks.BEDROCK))
                                    level.destroyBlock(blockPos, true);
                            }
                        }
                    }
                    sound_happen = true;
                    event.setAmount(0);
                } else if (fall_safe && damageSource.is(DamageTypes.FALL)) {
                    int x = livingEntity.getBlockX();
                    int y = livingEntity.getBlockY();
                    int z = livingEntity.getBlockZ();
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            for (int k = -1; k < 2; k++) {
                                BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                                if (!level.getBlockState(blockPos).is(Blocks.BEDROCK))
                                    level.destroyBlock(blockPos, true);
                            }
                        }
                    }
                    sound_happen = true;
                    event.setAmount(0);
                    fall_safe = false;
                } else if (damageSource.is(DamageTypes.FALLING_ANVIL))
                    event.setAmount(0);
                livingEntity.getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak(5, livingEntity,
                        p -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
            }
        }
        @SubscribeEvent
        public static void test2(LivingFallEvent event){
            LivingEntity livingEntity = event.getEntity();
            if(livingEntity.level().isClientSide)
                fall_safe = livingEntity.isFallFlying() && livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.SAFETY_HELMET.get();
        }
        @SubscribeEvent
        public static void test3(LivingEvent.LivingTickEvent event){
            Level level = event.getEntity().level();
            LivingEntity livingEntity = event.getEntity();
            if(level.isClientSide && sound_happen && livingEntity instanceof Player){
                level.playSound((Player) livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS);
                sound_happen = false;
            }
        }
    }
}