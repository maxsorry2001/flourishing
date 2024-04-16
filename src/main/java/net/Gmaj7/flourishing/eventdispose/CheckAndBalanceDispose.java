package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.Gmaj7.flourishing.flourishingEffect.FlourishingEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class CheckAndBalanceDispose {
    static Potion[] potions = {Potions.NIGHT_VISION, Potions.FIRE_RESISTANCE, Potions.HARMING, Potions.HEALING, Potions.INVISIBILITY,
                                Potions.LEAPING, Potions.LUCK, Potions.REGENERATION, Potions.SLOWNESS, Potions.STRENGTH,
                                Potions.SLOW_FALLING, Potions.SWIFTNESS, Potions.TURTLE_MASTER, Potions.WATER_BREATHING, Potions.WEAKNESS};
    @SubscribeEvent
    public static void getnew(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack itemStackMain = player.getMainHandItem();
        int cabFlag = EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.CHECK_AND_BALABCE.get(), itemStackMain);
        if(!player.level().isClientSide() && itemStackMain.getItem() instanceof ProjectileWeaponItem
                && player.getOffhandItem().getItem() == Items.ARROW
                && cabFlag > 0
                && !player.hasEffect(FlourishingEffects.CHECK_AND_BALANCE.get())){
            for (int i = 0; i < player.getOffhandItem().getCount() + (cabFlag == 2 ? player.getOffhandItem().getCount() / 16 * 4 :0); i++){
                ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW);
                PotionUtils.setPotion(itemStack, potions[new Random().nextInt(potions.length)]);
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStack);
                player.level().addFreshEntity(itemEntity);
            }
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            player.addEffect(new MobEffectInstance(FlourishingEffects.CHECK_AND_BALANCE.get(), 1200, 0));
        }
    }
}
