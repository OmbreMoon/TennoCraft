package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unchecked")
public interface IWeaponModHolder<T extends WeaponSchema> extends IModHolder<T> {

    @Override
    default T schema(ItemStack stack) {
        var holder = this.schemaHolder(stack);
        return holder != null ? holder.schema() : null;
    }

    @Override
    default SchemaHolder<T> schemaHolder(ItemStack itemStack)  {
        return (SchemaHolder<T>) itemStack.get(TCData.SCHEMA);
    }

    default boolean isSchemaLoaded(ItemStack stack) {
        return this.schema(stack) != null;
    }

    default TriggerType<?> getTriggerType(LivingEntity entity, ItemStack stack) {
        return null;
    }
}
