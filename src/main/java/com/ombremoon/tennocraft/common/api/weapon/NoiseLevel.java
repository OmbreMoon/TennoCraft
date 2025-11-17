package com.ombremoon.tennocraft.common.api.weapon;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum NoiseLevel implements StringRepresentable {
    ALARMING("alarming"),
    SILENT("silent");

    public static final Codec<NoiseLevel> CODEC = StringRepresentable.fromEnum(NoiseLevel::values);
    private final String name;

    NoiseLevel(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
