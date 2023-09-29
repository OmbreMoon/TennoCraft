package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class VoltFrameItem extends FrameArmorItem<VoltFrameItem> {
    private final Supplier<List<AbilityType<?>>> voltAbilities = () -> new ArrayList<>() {{
        add(FrameAbilities.SPEED_ABILITY.get());
    }};

    public VoltFrameItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
        this.frameEnergy = 1000;
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.VOLT;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return voltAbilities;
    }
}
