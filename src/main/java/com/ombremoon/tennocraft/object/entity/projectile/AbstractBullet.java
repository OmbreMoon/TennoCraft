package com.ombremoon.tennocraft.object.entity.projectile;

import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AbstractBullet extends Entity {
    public AbstractBullet(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractBullet(EntityType<?> pEntityType, Level pLevel, LivingEntity shooter, ItemStack weaponStack, AbstractProjectileWeapon projectileWeapon) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
