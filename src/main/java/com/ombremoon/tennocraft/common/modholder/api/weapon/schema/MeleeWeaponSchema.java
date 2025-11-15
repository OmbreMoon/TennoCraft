package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

public class MeleeWeaponSchema extends WeaponSchema {
    MeleeWeaponSchema(GeneralSchema generalSchema) {
        super(generalSchema);
    }

    @Override
    public SchemaSerializer<?> getSerializer() {
        return null;
    }
}
