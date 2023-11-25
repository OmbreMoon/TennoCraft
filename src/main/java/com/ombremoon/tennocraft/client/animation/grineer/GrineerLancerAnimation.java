package com.ombremoon.tennocraft.client.animation.grineer;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class GrineerLancerAnimation {
	public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2f).looping()
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-22.23f, -11.03f, -28.08f),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("gun",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(8.03f, -47.57f, -46.68f),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-19.43f, 7.38f, 10.29f),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("torso",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM))).build();
	public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(1f).looping()
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(-12.41f, -0.88f, -1.62f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-12.41f, 0.88f, 1.62f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.degreeVec(-12.41f, 0.88f, 1.62f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 1f, -1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, -1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 1f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(-12.41f, -0.88f, -1.62f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, -1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 1f, -1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, -1f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-12.41f, 0.88f, 1.62f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.degreeVec(-12.41f, 0.88f, 1.62f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("torso",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.1f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.6f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM))).build();
	public static final AnimationDefinition SHOOT = AnimationDefinition.Builder.withLength(3f)
			.addAnimation("body",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -3f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(0f, -3f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.posVec(0f, -3f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("torso",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("torso",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.85f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.05f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.15f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.2f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.35f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.45f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.6f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.65f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.8f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.9f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.95f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.05f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.1f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("head",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.85f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.05f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.15f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.2f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.3f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.35f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.45f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.6f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.65f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.8f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.9f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.95f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.05f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.1f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(1f, 0f, 1f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(1f, 0f, 1f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.posVec(1f, 0f, 1f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-22.23f, -11.03f, -28.08f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(-90f, -22.5f, -10f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(-90f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.85f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.05f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.15f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.2f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.3f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.35f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.45f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.6f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.65f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.8f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.9f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.95f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.05f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.1f, KeyframeAnimations.degreeVec(-85f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.2f, KeyframeAnimations.degreeVec(-89.17f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.degreeVec(-90f, -22.5f, -10f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(-22.23f, -11.03f, -28.08f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("gun",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(8.03f, -47.57f, -46.68f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(22.5f, 0f, 22.5f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(22.5f, 0f, 22.5f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.degreeVec(22.5f, 0f, 22.5f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(8.03f, -47.57f, -46.68f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, -3f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, -3f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.posVec(0f, 0f, -3f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(-19.43f, 7.38f, 10.29f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(-85.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(-85.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.85f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(0.9f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.05f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.15f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.2f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.3f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.35f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.45f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.5f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.6f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.65f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.75f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.8f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.9f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(1.95f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.05f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.1f, KeyframeAnimations.degreeVec(-80.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.2f, KeyframeAnimations.degreeVec(-86.69f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.degreeVec(-85.03f, 52.28f, 13.83f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(-19.43f, 7.38f, 10.29f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg",
					new AnimationChannel(AnimationChannel.Targets.POSITION,
							new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 2f, -2f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 2f, -2f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.posVec(0f, 2f, -2f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(7.6f, -1.62f, -7.32f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(7.6f, -1.62f, -7.32f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.degreeVec(7.6f, -1.62f, -7.32f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.5f, KeyframeAnimations.degreeVec(37.39f, -3.04f, 3.97f),
									AnimationChannel.Interpolations.CATMULLROM),
							new Keyframe(0.75f, KeyframeAnimations.degreeVec(37.39f, -3.04f, 3.97f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(2.25f, KeyframeAnimations.degreeVec(37.39f, -3.04f, 3.97f),
									AnimationChannel.Interpolations.LINEAR),
							new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
									AnimationChannel.Interpolations.CATMULLROM))).build();
}