package com.ombremoon.tennocraft.common.api;


import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IModHolder<T extends Schema> {

    Modification.Compatibility getModType();

    SchemaHolder<T> schemaHolder(ItemStack itemStack);

    T schema(ItemStack stack);

    ModContainer getMods(LivingEntity entity, @Nullable ItemStack stack);

    default ModContainer getMods(LivingEntity entity) {
        return this.getMods(entity, null);
    }

    void confirmModChanges(Player player, ItemStack stack);

}
