package com.ombremoon.tennocraft.client.model;

import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BulletModel extends GeoModel<BulletProjectile> {
    @Override
    public ResourceLocation getModelResource(BulletProjectile animatable) {
        SolidProjectile projectile = animatable.getData(TCData.PROJECTILE);
        Bullet bullet = projectile.bullet().value();
        if (bullet.modelLocation().isPresent())
            return bullet.modelLocation().get();

        return CommonClass.customLocation("geo/entity/generic_bullet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BulletProjectile animatable) {
        SolidProjectile projectile = animatable.getData(TCData.PROJECTILE);
        Bullet bullet = projectile.bullet().value();
        if (bullet.textureLocation().isPresent())
            return bullet.textureLocation().get();

        return CommonClass.customLocation("textures/entity/generic_bullet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BulletProjectile animatable) {
        return null;
    }
}
