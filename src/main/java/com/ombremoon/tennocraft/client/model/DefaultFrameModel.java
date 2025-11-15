package com.ombremoon.tennocraft.client.model;

import com.ombremoon.tennocraft.common.world.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DefaultFrameModel<T extends TransferenceKeyItem> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return CommonClass.customLocation("geo/frame/volt.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return CommonClass.customLocation("textures/frame/volt.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }
}
