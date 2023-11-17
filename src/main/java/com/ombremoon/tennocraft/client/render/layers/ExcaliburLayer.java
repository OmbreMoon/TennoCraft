package com.ombremoon.tennocraft.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.client.model.frames.ExcaliburModel;
import com.ombremoon.tennocraft.object.item.mineframe.ExcaliburFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.VoltFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.ExcaliburHelmetItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.VoltHelmetItem;
import net.minecraft.client.Minecraft;
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

public class ExcaliburLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation EXCALIBUR_LOCATION = TennoCraft.customLocation("textures/entity/frames/excalibur.png");
    private final ExcaliburModel<T> excaliburModel;

    public ExcaliburLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.excaliburModel = new ExcaliburModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ExcaliburModel.EXCALIBUR_LAYER_LOCATION));
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
            if (frameItem instanceof ExcaliburFrameItem || frameItem instanceof ExcaliburHelmetItem) {
                this.renderFrameModel(poseStack, bufferSource, livingEntity, equipmentSlot, packedLight, itemStack);
            }
        }
    }

    private void renderFrameModel(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot equipmentSlot, int packedLight, ItemStack frameStack) {
        poseStack.pushPose();
        this.getParentModel().copyPropertiesTo(this.excaliburModel);
        this.setPartVisibility(this.excaliburModel, equipmentSlot);
        this.excaliburModel.setupAnim(livingEntity, packedLight, packedLight, packedLight, packedLight, packedLight);
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(getExcaliburLocation()), false, frameStack.hasFoil());
        this.excaliburModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    protected void setPartVisibility(HumanoidModel<T> excaliburModel, EquipmentSlot equipmentSlot) {
        excaliburModel.setAllVisible(false);
        switch (equipmentSlot) {
            case HEAD -> {
                excaliburModel.head.visible = true;
            }
            case CHEST -> {
                excaliburModel.body.visible = true;
                excaliburModel.leftArm.visible = true;
                excaliburModel.rightArm.visible = true;
            }
            case LEGS -> {
                excaliburModel.leftLeg.visible = true;
                excaliburModel.rightLeg.visible = true;
            }
            case FEET -> {
                excaliburModel.rightLeg.visible = true;
                excaliburModel.leftLeg.visible = true;
            }
        }
    }

    public ResourceLocation getExcaliburLocation() { return EXCALIBUR_LOCATION; }
}
