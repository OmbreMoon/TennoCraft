package com.ombremoon.tennocraft.client.renderer;

import com.ombremoon.tennocraft.client.model.BulletModel;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulletRenderer extends GeoEntityRenderer<BulletProjectile> {
    public BulletRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BulletModel());
    }
}
