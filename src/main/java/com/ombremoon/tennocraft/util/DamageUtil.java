package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.player.FrameAttribute;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamageUtil {

    public static void doPostHurtEffects(FrameAttribute frameAttribute, LivingEntity target, Entity attacker) {
        DamageUtil.AttributeVisitor attribute$visitor = (frameAttribute1) -> {
            frameAttribute1.doPostHurt(target, attacker);
        };
        attribute$visitor.accept(frameAttribute);
    }

    public static void doPostDamageEffects(FrameAttribute frameAttribute, LivingEntity attacker, Entity target) {
        DamageUtil.AttributeVisitor attribute$visitor = (frameAttribute1) -> {
            frameAttribute1.doPostAttack(attacker, target);
        };
        attribute$visitor.accept(frameAttribute);
    }

    @FunctionalInterface
    interface AttributeVisitor {
        void accept(FrameAttribute frameAttribute);
    }
}
