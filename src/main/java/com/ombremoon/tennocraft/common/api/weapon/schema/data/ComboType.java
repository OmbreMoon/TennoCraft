package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import net.minecraft.util.StringRepresentable;

public enum ComboType implements StringRepresentable {
    NEUTRAL("neutral"),
    FORWARD("forward"),
    HEAVY("heavy"),
    SLIDE("slide"),
    AERIAL("aerial"),
    WALL("wall"),
    SLAM("slam"),
    FINISHER("finisher");

    public static final StringRepresentable.EnumCodec<ComboType> CODEC = StringRepresentable.fromEnum(ComboType::values);
    private final String name;

    ComboType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static ComboType byName(String name) {
        return CODEC.byName(name);
    }
}
