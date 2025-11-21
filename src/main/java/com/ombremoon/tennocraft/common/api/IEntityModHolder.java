package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public interface IEntityModHolder<T extends Schema> extends IModHolder<T> {

    AttributeMap getStats();
}
