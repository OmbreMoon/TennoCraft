package com.ombremoon.tennocraft.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ombremoon.tennocraft.client.renderer.FrameRenderer;
import com.ombremoon.tennocraft.common.modholder.FrameHandler;
import com.ombremoon.tennocraft.common.world.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.util.Color;

public class PlayerFrameLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    private final FrameRenderer<? extends TransferenceKeyItem> frameRenderer;

    public PlayerFrameLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.frameRenderer = new FrameLayer<>();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        FrameHandler handler = FrameUtil.getFrameHandler(livingEntity);
        if (handler.isTransfered()) {
            ItemStack itemStack = handler.getOrCreateKey();
            renderFrameLayer(poseStack, multiBufferSource, livingEntity, itemStack, packedLight, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
        }
    }

    private void renderFrameLayer(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, ItemStack itemStack, int packedLight, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().copyPropertiesTo(frameRenderer);
        this.frameRenderer.prepForRender(livingEntity, itemStack, this.getParentModel(), bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
        this.frameRenderer.renderToBuffer(poseStack, null, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
    }
}
