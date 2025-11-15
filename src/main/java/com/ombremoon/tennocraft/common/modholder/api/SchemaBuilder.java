package com.ombremoon.tennocraft.common.modholder.api;

import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;

public interface SchemaBuilder<T extends Schema> {

    T build();
}
