package com.ombremoon.tennocraft.common.world;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class TennoSlots extends HashMap<SlotGroup, IModHolder<?>> {
    private WeaponSchema selectedWeapon;

    public boolean switchSlots(Player player, SlotGroup group, IModHolder<?> modHolder) {
        return this.switchSlots(player, group, modHolder, null);
    }

    public boolean switchSlots(Player player, SlotGroup group, IModHolder<?> modHolder, @Nullable ItemStack stack) {
        if (modHolder.getModType().getGroup() != group) {
            return false;
        }

        this.put(group, modHolder);
        if (stack != null && group == SlotGroup.WEAPON && modHolder instanceof IWeaponModHolder<?>) {
            this.selectedWeapon = (WeaponSchema) modHolder.schema(stack);

            WeaponModContainer mods = (WeaponModContainer) modHolder.getMods(stack);
            mods.collectModifiers(player, stack);
        }

        return true;
    }

}
