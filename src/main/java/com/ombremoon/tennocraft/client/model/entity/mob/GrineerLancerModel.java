package com.ombremoon.tennocraft.client.model.entity.mob;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.client.animation.grineer.GrineerLancerAnimation;
import com.ombremoon.tennocraft.object.entity.mob.grineer.GrineerLancerEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class GrineerLancerModel extends HierarchicalModel<GrineerLancerEntity> implements ArmedModel, HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(TennoCraft.customLocation("grineer_lancer"), "main");
	private final ModelPart grineer_lancer;
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart head;

	public GrineerLancerModel(ModelPart root) {
		this.grineer_lancer = root.getChild("grineer_lancer");
		this.body = grineer_lancer.getChild("body");
		this.torso = body.getChild("torso");
		this.head = torso.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition grineer_lancer = partdefinition.addOrReplaceChild("grineer_lancer", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = grineer_lancer.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(24, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(25, 58).addBox(-4.0F, -4.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.25F, 0.0F));

		PartDefinition cube_r1 = torso.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, 1.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(44, 12).addBox(-3.5F, -4.0F, -1.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -10.25F, 1.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r2 = torso.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 32).addBox(-4.5F, -4.0F, -2.0F, 9.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 4.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r3 = torso.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 0).addBox(-3.5F, -4.5F, -2.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -10.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition cube_r4 = torso.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(34, 48).addBox(-2.0F, -3.0F, -1.5F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -2.0F, 0.0F, 0.2182F, 0.5236F));

		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(4, 0).addBox(-3.5F, -4.5F, -4.1F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(4, 0).addBox(1.5F, -4.5F, -4.1F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(1, 16).addBox(0.5F, -6.5F, -1.0F, 2.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.25F, -0.5F, -3.5F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(1, 16).mirror().addBox(-2.5F, -6.5F, -1.0F, 2.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.25F, -0.5F, -3.5F, 0.0F, 0.0F, 0.3927F));

		PartDefinition right_arm = torso.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(26, 32).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition gun = right_arm.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(50, 46).addBox(1.0F, 1.0F, -1.0F, 0.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 44).addBox(0.0F, -3.0F, -3.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(0, 16).addBox(0.5F, 4.5F, -0.5F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 2).addBox(1.0F, 1.0F, -4.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 8.0F, -1.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r7 = gun.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 48).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_arm = torso.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(42, 32).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition cube_r8 = left_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(48, 20).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, -1.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		grineer_lancer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.grineer_lancer;
	}

	@Override
	public void setupAnim(GrineerLancerEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(pNetHeadYaw, pHeadPitch, pAgeInTicks);

		animate(pEntity.idleAnimationState, GrineerLancerAnimation.IDLE, pAgeInTicks);
//		animate(pEntity.shootAnimationState, GrineerLancerAnimation.SHOOT, pAgeInTicks);

		if (!pEntity.isInWaterOrBubble()) {
			animateWalk(GrineerLancerAnimation.WALK, pLimbSwing, pLimbSwingAmount, 1.0F, 2.5F);
		}
	}

	private void applyHeadRotation( float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack) {

	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}


}