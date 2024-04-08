package net.Gmaj7.interestingmod.eventdispose;

import net.Gmaj7.interestingmod.InterestingMod;
import net.Gmaj7.interestingmod.modEnchantment.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = InterestingMod.MODID)
public class CompanyDispose {
    @SubscribeEvent
    public static void old_to_new(PlayerDestroyItemEvent event){
        ItemStack itemStack = event.getOriginal();
        Player player = event.getEntity();
        int tt = itemStack.getDamageValue();
        if(EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.COMPANY.get(), itemStack) > 0){
            Map<Enchantment, Integer> enchantmentIntegerMap = itemStack.getAllEnchantments();
            ItemStack[][] itemStackPool ={{new ItemStack(Items.WOODEN_SHOVEL), new ItemStack(Items.STONE_SHOVEL),
                    new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.GOLDEN_SHOVEL),
                    new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.NETHERITE_SHOVEL)},
                    {new ItemStack(Items.WOODEN_PICKAXE), new ItemStack(Items.STONE_PICKAXE),
                    new ItemStack(Items.IRON_PICKAXE),new ItemStack(Items.GOLDEN_PICKAXE),
                    new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.NETHERITE_PICKAXE)}};
            int n = new Random().nextInt(6);
            ItemStack itemStackNew = itemStackPool[0][n];
            itemStackNew.setDamageValue(itemStackNew.getMaxDamage() - 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, itemStackNew);
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), itemStackNew);
            player.level().addFreshEntity(itemEntity);
        }
    }
}