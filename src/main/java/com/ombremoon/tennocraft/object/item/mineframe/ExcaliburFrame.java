package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.world.item.ArmorMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExcaliburFrame extends FrameArmorItem<ExcaliburFrame> {
    private final Supplier<List<AbilityType<?>>> excaliburAbilities = () -> new ArrayList<>() {{
        add(FrameAbilities.RADIAL_BLIND_ABILITY.get());
    }};

    public ExcaliburFrame(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.EXCALIBUR;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return excaliburAbilities;
    }
}
