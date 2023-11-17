package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.FrameHelmetItem;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID)
public class TCEvents {

    /*@SubscribeEvent
    public static void onFrameEquipped(LivingEquipmentChangeEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            if (FrameUtil.hasOnFrame(event.getEntity()) && event.getEntity() instanceof Player player) {
                ItemStack frameStack = FrameUtil.getFrameStack(player);
                CompoundTag compoundTag = frameStack.getOrCreateTag();
                FrameHelmetItem<?> frameHelmetItem = (FrameHelmetItem<?>)frameStack.getItem();
                if (!compoundTag.contains("Frame Attributes", 9)) {
                    compoundTag.put("Frame Attributes", new ListTag());
                    ListTag listTag = frameStack.getTag().getList(FrameUtil.FRAME_ATTR, 10);
                    listTag.add(FrameUtil.storeFrameAttribute(FrameUtil.getFrameAttributeId(FrameAttributes.ENERGY.get()), frameHelmetItem.getFrameType().getFrameEnergy()));
                }
            }
        }
    }*/
}
