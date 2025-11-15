package com.ombremoon.tennocraft.common.world.item;


import com.ombremoon.tennocraft.common.modholder.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IModHolder<T extends Schema> {

    Modification.Compatibility getModType();

    default SchemaHolder schema(ItemStack itemStack) {
        return itemStack.get(TCData.SCHEMA);
    }

    ModContainer getMods(@Nullable ItemStack stack);

    default ModContainer getMods() {
        return this.getMods(null);
    }

    AttributeMap getStats(@Nullable ItemStack stack);

    default AttributeMap getStats() {
        return this.getStats(null);
    }

    default boolean usesCustomItemModel() {
        return false;
    }

}
