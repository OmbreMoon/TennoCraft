package com.ombremoon.tennocraft.object.entity.projectile;

import com.ombremoon.tennocraft.common.init.entity.TCProjectiles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GenericBullet extends AbstractBullet {
    public GenericBullet(EntityType<? extends AbstractBullet> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
