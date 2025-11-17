package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum ModTarget implements StringRepresentable {
    ATTACKER("attacker", false),
    DAMAGING_ENTITY("damaging_entity", false),
    VICTIM("victim", false),
    SURROUNDING_ALLIES("allies", true),
    SURROUNDING_ENEMIES("enemies", true);

    public static final Codec<ModTarget> CODEC = StringRepresentable.fromEnum(ModTarget::values);
    private final String name;
    private final boolean auraCompatible;

    ModTarget(String name, boolean auraCompatible) {
        this.name = name;
        this.auraCompatible = auraCompatible;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isAuraCompatible() {
        return this.auraCompatible;
    }
}
