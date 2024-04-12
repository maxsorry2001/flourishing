package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.modEnchantment.ModEnchantments;
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
    static ItemStack[] itemStackPool ={new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.STONE_SHOVEL),
            new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.GOLDEN_SHOVEL),
            new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.NETHERITE_SHOVEL),
            new ItemStack(Items.WOODEN_PICKAXE), new ItemStack(Items.STONE_PICKAXE),
            new ItemStack(Items.IRON_PICKAXE),new ItemStack(Items.GOLDEN_PICKAXE),
            new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.NETHERITE_PICKAXE),
            new ItemStack(Items.WOODEN_AXE), new ItemStack(Items.STONE_AXE),
            new ItemStack(Items.IRON_AXE),new ItemStack(Items.GOLDEN_AXE),
            new ItemStack(Items.DIAMOND_AXE), new ItemStack(Items.NETHERITE_AXE),
            new ItemStack(Items.WOODEN_SWORD), new ItemStack(Items.STONE_SWORD),
            new ItemStack(Items.IRON_SWORD),new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.WOODEN_HOE), new ItemStack(Items.STONE_HOE),
            new ItemStack(Items.IRON_HOE),new ItemStack(Items.GOLDEN_HOE),
            new ItemStack(Items.DIAMOND_HOE), new ItemStack(Items.NETHERITE_HOE)};
    @SubscribeEvent
    public static void old_to_new(PlayerDestroyItemEvent event){
        ItemStack itemStack = event.getOriginal();
        Player player = event.getEntity();
        if(EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.COMPANY.get(), itemStack) > 0){
            Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
            enchantmentIntegerMap.put(ModEnchantments.COMPANY.get(), 1);
            int n = new Random().nextInt(30);
            ItemStack itemStackNew = itemStackPool[n];
            itemStackNew.setDamageValue(itemStackNew.getMaxDamage() - 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, itemStackNew);
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStackNew);
            player.level().addFreshEntity(itemEntity);
        }
    }
}