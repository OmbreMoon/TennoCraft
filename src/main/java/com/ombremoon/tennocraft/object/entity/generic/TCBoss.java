package com.ombremoon.tennocraft.object.entity.generic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class TCBoss extends TCEnemy<TCBoss> {
    public TCBoss(EntityType<? extends TCEnemy> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
