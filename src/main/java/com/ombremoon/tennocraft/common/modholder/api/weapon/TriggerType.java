package com.ombremoon.tennocraft.common.modholder.api.weapon;

import net.minecraft.util.StringRepresentable;

public enum TriggerType implements StringRepresentable {
    AUTO("auto"),
    SEMI("semi"),
    BURST("burst"),
    CHARGE("charge"),
    HELD("held"),
    DUPLEX("duplex"),
    ACTIVE("active"),
    MAG_BURST("mag_burst"),
    AUTO_SPOOL("auto_spool"),
    AUTO_BURST("auto_burst");

    public static final StringRepresentable.EnumCodec<TriggerType> CODEC = StringRepresentable.fromEnum(TriggerType::values);
    private final String name;

    TriggerType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static TriggerType byName(String name) {
        return CODEC.byName(name);
    }
}
