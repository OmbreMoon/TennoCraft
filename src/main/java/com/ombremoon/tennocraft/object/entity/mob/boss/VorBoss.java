package com.ombremoon.tennocraft.object.entity.mob.boss;

import com.ombremoon.tennocraft.object.entity.generic.TCBoss;
import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class VorBoss extends TCBoss {
    public VorBoss(EntityType<? extends TCEnemy> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

}
