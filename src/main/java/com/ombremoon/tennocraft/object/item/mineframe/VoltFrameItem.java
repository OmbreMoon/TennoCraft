package com.ombremoon.tennocraft.object.item.mineframe;

import net.minecraft.world.entity.EquipmentSlot;

public class VoltFrameItem extends FrameArmorItem<VoltFrameItem> {

    public VoltFrameItem(EquipmentSlot equipmentSlot, Properties pProperties) {
        super(equipmentSlot, pProperties);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.VOLT;
    }
}
