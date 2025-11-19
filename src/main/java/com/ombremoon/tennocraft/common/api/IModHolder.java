package com.ombremoon.tennocraft.common.api;


import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public interface IModHolder<T extends Schema> {

    Modification.Compatibility getModType();

    default SchemaHolder<T> schemaHolder(ItemStack itemStack) {
        return (SchemaHolder<T>) itemStack.get(TCData.SCHEMA);
    }

    T schema(ItemStack stack);

    ModContainer getMods(@Nullable ItemStack stack);

    void confirmModChanges(Level level, ItemStack stack);

    default ModContainer getMods() {
        return this.getMods(null);
    }

    AttributeMap getStats(@Nullable ItemStack stack);

    default AttributeMap getStats() {
        return this.getStats(null);
    }

}
