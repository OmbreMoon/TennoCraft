package com.ombremoon.tennocraft.client.model;

import com.ombremoon.tennocraft.common.world.item.weapon.AbstractWeapon;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DefaultWeaponModel extends GeoModel<AbstractWeapon<?>> {

    @Override
    public ResourceLocation getModelResource(AbstractWeapon<?> animatable) {
        return CommonClass.customLocation("geo/schema/atomos.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractWeapon<?> animatable) {
        return CommonClass.customLocation("textures/schema/atomos.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractWeapon<?> animatable) {
        return null;
    }
}
