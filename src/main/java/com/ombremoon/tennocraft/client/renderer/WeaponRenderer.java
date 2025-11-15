package com.ombremoon.tennocraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.tennocraft.client.model.DefaultWeaponModel;
import com.ombremoon.tennocraft.common.world.item.weapon.AbstractWeapon;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.math.MolangQueries;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WeaponRenderer extends GeoItemRenderer<AbstractWeapon<?>> {
    public WeaponRenderer() {
        super(new DefaultWeaponModel());
    }

    @Override
    public void defaultRender(PoseStack poseStack, AbstractWeapon<?> animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        poseStack.pushPose();

        int renderColor = getRenderColor(animatable, partialTick, packedLight).argbInt();
        int packedOverlay = getPackedOverlay(animatable, 0, partialTick);
        BakedGeoModel model = getGeoModel().getBakedModel(this.getModelResource(animatable));

        if (renderType == null)
            renderType = getRenderType(animatable, getTextureLocation(animatable), bufferSource, partialTick);

        if (buffer == null && renderType != null)
            buffer = bufferSource.getBuffer(renderType);

        preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);

        if (firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, packedLight, packedLight, packedOverlay);
            actuallyRender(poseStack, animatable, model, renderType,
                    bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);
            applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, renderColor);
            firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }

        poseStack.popPose();

        renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
        doPostRenderCleanup();
        MolangQueries.clearActor();
    }

    public ResourceLocation getModelResource(AbstractWeapon<?> animatable) {
        if (this.currentItemStack != null) {
            ResourceLocation location = FrameUtil.getResource(this.currentItemStack, "geo/schema", ".geo.json");
            if (location != null)
                return location;
        }

        return getGeoModel().getModelResource(animatable, this);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWeapon<?> animatable) {
        if (this.currentItemStack != null) {
            ResourceLocation location = FrameUtil.getResource(this.currentItemStack, "textures/schema", ".png");
            if (location != null)
                return location;
        }

        return super.getTextureLocation(animatable);
    }
}
