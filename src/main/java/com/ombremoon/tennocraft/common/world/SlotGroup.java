package com.ombremoon.tennocraft.common.world;

import net.minecraft.util.StringRepresentable;

public enum SlotGroup implements StringRepresentable {
    FRAME("frame"),
    WEAPON("weapon"),
    VEHICLE("vehicle"),
    COMPANION("companion"),
    COMPANION_WEAPON("companion_weapon");

    public static final StringRepresentableCodec<SlotGroup> CODEC = StringRepresentable.fromEnum(SlotGroup::values);
    private final String name;

    SlotGroup(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
