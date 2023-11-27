package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import com.ombremoon.tennocraft.object.entity.projectile.AbstractBullet;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID)
public class TCEvents {

    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        String type = event.getIdentifier();
        ItemStack newTransferenceKey = event.getTo();
        if (type.equals(FrameUtil.CURIO_SLOT)) {
            if (newTransferenceKey.getItem() instanceof TransferenceKeyItem item) {
                FrameUtil.initFrameAttributes(newTransferenceKey, item.getFrameType());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (FrameUtil.hasOnFrame(player)) {
                event.setDamageMultiplier(0);
            }
        }
    }

    @SubscribeEvent
    public static void onEnemySpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof TCEnemy<?> enemy) {
                System.out.println("FOR THE QUEENS!");
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractBullet) {
            System.out.println("FOR THE QUEENS!");
        }
    }
}
