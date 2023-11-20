package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID)
public class TCEvents {

    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        String type = event.getIdentifier();
        LivingEntity livingEntity = event.getEntity();
        ItemStack transferenceKey = event.getFrom();
        if (type.equals(FrameUtil.CURIO_SLOT)) {
            if (livingEntity.getTags().contains(FrameUtil.TRANSFERENCE) && transferenceKey.getItem() instanceof TransferenceKeyItem) {
                livingEntity.getTags().remove(FrameUtil.TRANSFERENCE);
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
}
