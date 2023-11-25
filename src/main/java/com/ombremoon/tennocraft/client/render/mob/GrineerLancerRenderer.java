package com.ombremoon.tennocraft.client.render.mob;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.client.model.entity.mob.GrineerLancerModel;
import com.ombremoon.tennocraft.object.entity.mob.grineer.GrineerLancerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrineerLancerRenderer extends MobRenderer<GrineerLancerEntity, GrineerLancerModel> {
    private static final ResourceLocation TEXTURE = TennoCraft.customLocation("textures/entity/mob/grineer/grineer_lancer.png");

    public GrineerLancerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GrineerLancerModel(pContext.bakeLayer(GrineerLancerModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GrineerLancerEntity pEntity) {
        return TEXTURE;
    }
}
