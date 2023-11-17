package com.ombremoon.tennocraft.object.item.mineframe.helmet;

import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.object.item.mineframe.IModHolder;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FrameHelmetItem<T extends FrameArmorItem<T>> extends FrameArmorItem<T> implements IModHolder {
    protected Supplier<List<AbilityType<?>>> abilityList = ArrayList::new;
    protected float frameEnergy;

    public FrameHelmetItem(Properties pProperties) {
        super(EquipmentSlot.HEAD, pProperties);
    }

    public float getFrameEnergy() {
        return this.frameEnergy;
    }

    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return this.abilityList;
    }

    @Override
    public ModType getModType() {
        return ModType.MINEFRAME;
    }

}
