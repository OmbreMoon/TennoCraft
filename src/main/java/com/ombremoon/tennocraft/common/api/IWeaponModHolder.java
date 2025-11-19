package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import net.minecraft.world.item.ItemStack;

public interface IWeaponModHolder<T extends WeaponSchema> extends IModHolder<T> {

    @Override
    default T schema(ItemStack stack) {
        return this.schemaHolder(stack).schema();
    }
}
