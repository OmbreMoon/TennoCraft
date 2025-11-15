package com.ombremoon.tennocraft.common.modholder.api.mod;

import net.minecraft.util.StringRepresentable;

public enum ModRarity implements StringRepresentable {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    LEGENDARY("legendary"),
    REQUIEM("requiem");

    public static final StringRepresentableCodec<ModRarity> CODEC = StringRepresentable.fromEnum(ModRarity::values);
    private final String nane;

    ModRarity(String name) {
        this.nane = name;
    }

    @Override
    public String getSerializedName() {
        return this.nane;
    }
}
