package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class CompanyDispose {
    @SubscribeEvent
    public static void old_to_new(PlayerDestroyItemEvent event){
        ItemStack itemStack = event.getOriginal();
        Player player = event.getEntity();
        if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.COMPANY.get(), itemStack) > 0){
            Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
            enchantmentIntegerMap.put(FlourishingEnchantments.COMPANY.get(), 1);
            ItemStack itemStackNew = LootPool.itemStackEquip[new Random().nextInt(30)];
            itemStackNew.setDamageValue(itemStackNew.getMaxDamage() - 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, itemStackNew);
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStackNew);
            player.level().addFreshEntity(itemEntity);
        }
    }
}