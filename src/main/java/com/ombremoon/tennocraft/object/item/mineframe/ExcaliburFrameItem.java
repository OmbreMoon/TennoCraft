package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExcaliburFrameItem extends FrameArmorItem<ExcaliburFrameItem> {
    private final Supplier<List<AbilityType<?>>> excaliburAbilities = () -> new ArrayList<>() {{
//        add(FrameAbilities.RADIAL_BLIND_ABILITY.get());
    }};

    public ExcaliburFrameItem(EquipmentSlot equipmentSlot, Properties pProperties) {
        super(equipmentSlot, pProperties);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.EXCALIBUR;
    }

}