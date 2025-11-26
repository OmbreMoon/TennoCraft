package com.ombremoon.tennocraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Resource implements StringRepresentable {
    HEALTH("heath"),
    SHIELD("shield"),
    ENERGY("energy");

    public static final Codec<Resource> CODEC = StringRepresentable.fromEnum(Resource::values);
    private final String name;

    Resource(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
