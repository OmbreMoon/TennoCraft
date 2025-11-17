package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;

public interface SchemaBuilder<T extends Schema> {

    T build();
}
