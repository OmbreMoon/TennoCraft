package com.ombremoon.tennocraft.common.world;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TennoSlots extends HashMap<SlotGroup, IModHolder<?>> {
    private final List<SlotGroup> disabledSlots = new ArrayList<>();
    private WeaponSchema selectedWeapon;

    public boolean switchSlots(Player player, SlotGroup group, IModHolder<?> modHolder) {
        return this.switchSlots(player, group, modHolder, null);
    }

    public boolean switchSlots(Player player, SlotGroup group, IModHolder<?> modHolder, @Nullable ItemStack stack) {
        if (modHolder.getModType().getGroup() != group)
            return false;

        if (this.isDisabled(group))
            return false;

        this.put(group, modHolder);
        if (stack != null && group == SlotGroup.WEAPON && modHolder instanceof IWeaponModHolder<?>) {
            this.selectedWeapon = (WeaponSchema) modHolder.schema(stack);

            WeaponModContainer mods = (WeaponModContainer) modHolder.getMods(player, stack);
            if (mods != null)
                mods.collectModifiers(player, stack);
        }

        return true;
    }

    public void unequipFromSlot(SlotGroup group) {
        this.put(group, null);
    }

    public boolean isDisabled(SlotGroup group) {
        return this.disabledSlots.contains(group);
    }

    public boolean disableSlot(SlotGroup group) {
        return this.disabledSlots.remove(group);
    }

    public boolean enableSlot(SlotGroup group) {
        return this.disabledSlots.add(group);
    }

    public void disableAll() {
        for (SlotGroup group : SlotGroup.values()) {
            this.disableSlot(group);
        }
    }

    public void disableAllExcept(SlotGroup exception) {
        for (SlotGroup group : SlotGroup.values()) {
            if (group != exception) {
                this.disableSlot(group);
            }
        }
    }
}
