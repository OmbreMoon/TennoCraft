package com.ombremoon.tennocraft.object.entity.generic;

import com.ombremoon.tennocraft.object.entity.mob.FactionMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public abstract class TCEnemy<T extends TCEnemy<T>> extends Monster implements FactionMob {
    public TCEnemy(EntityType<? extends TCEnemy> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Faction getFaction() {
        return Faction.NONE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.MOVEMENT_SPEED);
    }

    public enum Faction {
        NONE,
        TENNO,
        GRINEER,
        CORPUS,
        INFECTED,
        OROKIN

    }
}
