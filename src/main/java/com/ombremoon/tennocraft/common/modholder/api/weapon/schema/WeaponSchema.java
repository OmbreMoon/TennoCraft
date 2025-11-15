package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

public abstract class WeaponSchema implements Schema {
    protected final GeneralSchema general;

    WeaponSchema(GeneralSchema general) {
        this.general = general;
    }

    public GeneralSchema getGeneral() {
        return this.general;
    }
}
