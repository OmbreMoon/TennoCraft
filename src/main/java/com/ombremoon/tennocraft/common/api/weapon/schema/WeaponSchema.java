package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import org.jetbrains.annotations.Nullable;

public abstract class WeaponSchema implements Schema {
    protected final GeneralSchema general;

    WeaponSchema(GeneralSchema general) {
        this.general = general;
    }

    public GeneralSchema getGeneral() {
        return this.general;
    }

    public abstract int getBaseDamage(@Nullable TriggerType triggerType);

    public int getBaseDamage() {
        return this.getBaseDamage(null);
    }
}
