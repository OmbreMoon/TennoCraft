package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.common.init.item.TCItems;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class FrameArmorItem<T extends FrameArmorItem<T>> extends ArmorItem {
    protected Supplier<List<AbilityType<?>>> abilityList = ArrayList::new;
    public int frameShield;
    public int frameArmor;
    public float frameEnergy;
    public float frameSpeed;

    public FrameArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public void setFrameEnergy(float frameEnergy) {
        this.frameEnergy = frameEnergy;
    }

    public float getFrameEnergy() {
        return this.frameEnergy;
    }

    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return this.abilityList;
    }

    public FrameType getFrameType() {
        return FrameType.NONE;
    }

    public enum FrameType {
        NONE(null),
        EXCALIBUR(TCFrames.EXCALIBUR_HELMET.get()),
        VOLT(TCFrames.VOLT_HELMET.get());

        private final Item frameArmorItem;

        FrameType(Item frameArmorItem) {
            this.frameArmorItem = frameArmorItem;
        }

        public Item getFrameArmorItem() {
            return frameArmorItem;
        }
    }
}
