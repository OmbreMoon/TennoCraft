package com.ombremoon.tennocraft.object.custom.attribute;

import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.RangedFrameAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class HeatAttribute extends RangedFrameAttribute {
    public HeatAttribute(@Nullable ResourceLocation frameAttribute, String descriptionId, float defaultValue, float minValue, float maxValue) {
        super(frameAttribute, descriptionId, defaultValue, minValue, maxValue);
    }

    @Override
    public boolean isElementalAttribute() {
        return true;
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.HEAT;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target) {
        super.doPostAttack(attacker, target);
        target.setSecondsOnFire(5);
        System.out.println("FOR THE QUEENS!");
    }
}
