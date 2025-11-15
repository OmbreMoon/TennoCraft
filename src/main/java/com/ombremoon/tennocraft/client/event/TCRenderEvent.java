package com.ombremoon.tennocraft.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ombremoon.tennocraft.client.renderer.FrameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public abstract class TCRenderEvent {

    public abstract static class FrameLayer extends Event implements GeoRenderEvent {
        private final FrameRenderer<?> renderer;

        public FrameLayer(FrameRenderer<?> renderer) {
            this.renderer = renderer;
        }

        @Override
        public FrameRenderer<?> getRenderer() {
            return this.renderer;
        }

        public LivingEntity getOwner() {
            return getRenderer().getCurrentEntity();
        }

        public static class Pre extends FrameLayer implements ICancellableEvent {
            private final PoseStack poseStack;
            private final BakedGeoModel model;
            private final MultiBufferSource bufferSource;
            private final float partialTick;
            private final int packedLight;

            public Pre(FrameRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        public static class Post extends FrameLayer {
            private final PoseStack poseStack;
            private final BakedGeoModel model;
            private final MultiBufferSource bufferSource;
            private final float partialTick;
            private final int packedLight;

            public Post(FrameRenderer<?> renderer, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
                super(renderer);
                this.poseStack = poseStack;
                this.model = model;
                this.bufferSource = bufferSource;
                this.partialTick = partialTick;
                this.packedLight = packedLight;
            }

            public PoseStack getPoseStack() {
                return this.poseStack;
            }

            public BakedGeoModel getModel() {
                return this.model;
            }

            public MultiBufferSource getBufferSource() {
                return this.bufferSource;
            }

            public float getPartialTick() {
                return this.partialTick;
            }

            public int getPackedLight() {
                return this.packedLight;
            }
        }

        public static class CompileRenderLayers extends FrameLayer {
            public CompileRenderLayers(FrameRenderer<?> renderer) {
                super(renderer);
            }

            public void addLayer(GeoRenderLayer renderLayer) {
                getRenderer().addRenderLayer(renderLayer);
            }
        }
    }
}
