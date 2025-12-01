package com.ombremoon.tennocraft.common.world.entity;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeployableProjectile extends BulletProjectile {
    public DeployableProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public DeployableProjectile(Level level, LivingEntity shooter, RangedAttack attackData, SolidProjectile projectile, ItemStack firedFromWeapon, TriggerType<?> triggerType) {
        super(level, shooter, attackData, projectile, firedFromWeapon, triggerType);
    }
}
