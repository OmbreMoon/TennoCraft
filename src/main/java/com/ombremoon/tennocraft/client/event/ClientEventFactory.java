package com.ombremoon.tennocraft.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ombremoon.tennocraft.client.renderer.FrameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.neoforge.common.NeoForge;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class ClientEventFactory {

    public static boolean fireFrameLayerPreRender(FrameRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !NeoForge.EVENT_BUS.post(new TCRenderEvent.FrameLayer.Pre(renderer, poseStack, model, bufferSource, partialTick, packedLight)).isCanceled();
    }

    public static void fireFrameLayerPostRender(FrameRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        NeoForge.EVENT_BUS.post(new TCRenderEvent.FrameLayer.Post(renderer, poseStack, model, bufferSource, partialTick, packedLight));
    }

    public static void fireFrameLayerCompileRenderLayers(FrameRenderer<?> renderer) {
        NeoForge.EVENT_BUS.post(new TCRenderEvent.FrameLayer.CompileRenderLayers(renderer));
    }
}
