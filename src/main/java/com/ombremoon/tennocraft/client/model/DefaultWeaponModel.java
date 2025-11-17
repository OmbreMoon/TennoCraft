package com.ombremoon.tennocraft.client.model;

import com.ombremoon.tennocraft.common.world.item.weapon.AbstractWeaponItem;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DefaultWeaponModel extends GeoModel<AbstractWeaponItem<?>> {

    @Override
    public ResourceLocation getModelResource(AbstractWeaponItem<?> animatable) {
        return CommonClass.customLocation("geo/schema/atomos.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractWeaponItem<?> animatable) {
        return CommonClass.customLocation("textures/schema/atomos.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractWeaponItem<?> animatable) {
        return null;
    }
}
