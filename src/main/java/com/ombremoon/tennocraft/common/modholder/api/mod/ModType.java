package com.ombremoon.tennocraft.common.modholder.api.mod;

import net.minecraft.util.StringRepresentable;

public enum ModType implements StringRepresentable {
    STANDARD("standard"),
    AURA("aura"),
    EXILUS("aura"),
    STANCE("stance"),
    SET("set");

    public static final StringRepresentableCodec<ModType> CODEC = StringRepresentable.fromEnum(ModType::values);
    private final String name;

    ModType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
