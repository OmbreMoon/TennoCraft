package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.modholder.FrameHandler;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FrameUtil {

    public static FrameHandler getFrameHandler(LivingEntity livingEntity) {
        var handler = livingEntity.getData(TCData.FRAME_HANDLER);
        if (!handler.isInitialized())
            handler.initData(livingEntity);

        return handler;
    }

    public static ResourceLocation getResource(ItemStack stack, String directory, String type) {
        SchemaHolder schema = stack.get(TCData.SCHEMA);
        if (schema != null) {
            ResourceLocation location = schema.location();
            return ResourceLocation.fromNamespaceAndPath(
                    location.getNamespace(),
                    location.getPath().replace(schema.type(), directory) + type);
        }

        return null;
    }
}
