package com.ombremoon.tennocraft.client.render.frame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.client.model.frames.VoltModel;
import com.ombremoon.tennocraft.common.network.packet.client.data.TransferenceSyncData;
import com.ombremoon.tennocraft.object.item.transference.VoltKey;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VoltLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation VOLT_LOCATION = TennoCraft.customLocation("textures/entity/frames/volt.png");
    private final VoltModel<T> voltModel;

    public VoltLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.voltModel = new VoltModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(VoltModel.VOLT_LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        renderFramePiece(pPoseStack, pBuffer, pLivingEntity, pPackedLight);
    }

    private void renderFramePiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, int packedLight) {
        ItemStack frameStack = FrameUtil.getFrameStack((Player) livingEntity);
        Item transferenceKeyItem = frameStack.getItem();
        if (transferenceKeyItem instanceof VoltKey && TransferenceSyncData.getHasOnFrame()) {
            this.renderFrameModel(poseStack, bufferSource, livingEntity, packedLight, frameStack);
        }
    }

    private void renderFrameModel(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, int packedLight, ItemStack frameStack) {
        poseStack.pushPose();
        this.getParentModel().copyPropertiesTo(this.voltModel);
        this.voltModel.setupAnim(livingEntity, packedLight, packedLight, packedLight, packedLight, packedLight);
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(getVoltLocation()), false, frameStack.hasFoil());
        this.voltModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    public ResourceLocation getVoltLocation() { return VOLT_LOCATION; }
}
