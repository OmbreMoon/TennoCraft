package com.ombremoon.tennocraft.common.network.weapon;

public enum WeaponType {
    MELEE(0.0F, 0, false),
    SEMIAUTO(10.0F, 10, false),
    AUTOMATIC(20.0F, 30, true),
    CHARGE(10.0F, 15, true),
    THROWN(2.5F, 5, false);

    private final float projectileSpeed;
    private final int projectileLife;
    private final boolean isAutoFire;

    WeaponType(float projectileSpeed, int projectileLife, boolean isAutoFire) {
        this.projectileSpeed = projectileSpeed;
        this.projectileLife = projectileLife;
        this.isAutoFire = isAutoFire;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }

    public int getProjectileLife() {
        return this.projectileLife;
    }

    public boolean isAutoFire() {
        return this.isAutoFire;
    }
}
