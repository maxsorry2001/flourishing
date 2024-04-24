package net.Gmaj7.flourishing.eventdispose;

import net.Gmaj7.flourishing.Flourishing;
import net.Gmaj7.flourishing.flourishingEnchantment.FlourishingEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Flourishing.MODID)
public class TargetChangeDispose {
    @SubscribeEvent
    public static void ChangeDispose(LivingChangeTargetEvent event){
        LivingEntity livingEntity = event.getEntity();
        LivingEntity target = event.getNewTarget();
        if(EnchantmentHelper.getTagEnchantmentLevel(FlourishingEnchantments.SEE_CLEARLY.get(), livingEntity.getMainHandItem()) > 0
            && livingEntity instanceof Monster && target instanceof Player){
            List<Monster> list = livingEntity.level().getEntitiesOfClass(Monster.class, new AABB(livingEntity.getX() -20, livingEntity.getY() - 3, livingEntity.getZ() - 20, livingEntity.getX() + 20, livingEntity.getY() + 3, livingEntity.getZ() + 20));
            for (Monster monster : list){
                if(monster == livingEntity){
                    event.setNewTarget(null);
                    continue;
                }
                event.setNewTarget(monster);
                break;
            }
        }
    }
}
