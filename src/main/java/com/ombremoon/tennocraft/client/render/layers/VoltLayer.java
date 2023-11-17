package com.ombremoon.tennocraft.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.client.model.frames.VoltModel;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.object.item.mineframe.VoltFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.VoltHelmetItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
        renderFramePiece(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight);
        renderFramePiece(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.CHEST, pPackedLight);
        renderFramePiece(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.LEGS, pPackedLight);
        renderFramePiece(pPoseStack, pBuffer, pLivingEntity, EquipmentSlot.FEET, pPackedLight);
    }

    private void renderFramePiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot equipmentSlot, int packedLight) {
        ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
        Item frameItem = itemStack.getItem();
        if (frameItem.getEquipmentSlot(itemStack) == equipmentSlot) {
            if (frameItem instanceof VoltFrameItem || frameItem instanceof VoltHelmetItem) {
                this.renderFrameModel(poseStack, bufferSource, livingEntity, equipmentSlot, packedLight, itemStack);
            }
        }
    }

    private void renderFrameModel(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot equipmentSlot, int packedLight, ItemStack frameStack) {
        poseStack.pushPose();
        this.getParentModel().copyPropertiesTo(this.voltModel);
        this.setPartVisibility(this.voltModel, equipmentSlot);
        this.voltModel.setupAnim(livingEntity, packedLight, packedLight, packedLight, packedLight, packedLight);
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(getVoltLocation()), false, frameStack.hasFoil());
        this.voltModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    protected void setPartVisibility(VoltModel<T> voltModel, EquipmentSlot equipmentSlot) {
        voltModel.setAllVisible(false);
        switch (equipmentSlot) {
            case HEAD -> {
                voltModel.head.visible = true;
            }
            case CHEST -> {
                voltModel.body.visible = true;
                voltModel.leftArm.visible = true;
                voltModel.rightArm.visible = true;
            }
            case LEGS -> {
                voltModel.leftLeg.visible = true;
                voltModel.rightLeg.visible = true;
            }
            case FEET -> {
                voltModel.left_foot.visible = true;
                voltModel.right_foot.visible = true;
            }
        }
    }

    public ResourceLocation getVoltLocation() { return VOLT_LOCATION; }
}
