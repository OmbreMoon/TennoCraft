package com.ombremoon.tennocraft.common.world.item.weapon;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum WeaponSlot implements StringRepresentable {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    MELEE("melee");

    public static final Codec<WeaponSlot> CODEC = StringRepresentable.fromEnum(WeaponSlot::values);
    private final String name;

    WeaponSlot(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
