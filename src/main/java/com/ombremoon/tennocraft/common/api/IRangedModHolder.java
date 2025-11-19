package com.ombremoon.tennocraft.common.api;


import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import net.minecraft.world.item.ItemStack;

public interface IRangedModHolder extends IWeaponModHolder<RangedWeaponSchema> {

    TriggerType getTriggerType(ItemStack stack);
}
