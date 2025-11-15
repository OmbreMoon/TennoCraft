package com.ombremoon.tennocraft.common.modholder.api.weapon;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Accuracy implements StringRepresentable {
    VERY_HIGH("very_high", 0, 1),
    HIGH("high", 1, 3),
    MEDIUM("medium", 4, 6),
    LOW("low", 6, 10),
    VERY_LOW("very_low", 12, 20);

    public static final Codec<Accuracy> CODEC = StringRepresentable.fromEnum(Accuracy::values);
    private final String name;
    private final int minDeviation;
    private final int maxDeviation;

    Accuracy(String name, int minDeviation, int maxDeviation) {
        this.name = name;
        this.minDeviation = minDeviation;
        this.maxDeviation = maxDeviation;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int getMinDeviation() {
        return this.minDeviation;
    }

    public int getMaxDeviation() {
        return this.maxDeviation;
    }
}
