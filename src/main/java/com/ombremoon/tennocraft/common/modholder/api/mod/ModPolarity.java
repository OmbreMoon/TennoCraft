package com.ombremoon.tennocraft.common.modholder.api.mod;

import net.minecraft.util.StringRepresentable;

public enum ModPolarity implements StringRepresentable {
    MADURAI("madurai"),
    VAZARIN("vazarin"),
    NARAMON("naramon"),
    ZENURIK("zenurik"),
    UNAIRU("unairu"),
    PENJAGA("penjaga"),
    UMBRA("umbra"),
    ANY("any");

    public static final StringRepresentableCodec<ModPolarity> CODEC = StringRepresentable.fromEnum(ModPolarity::values);
    private final String name;

    ModPolarity(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
