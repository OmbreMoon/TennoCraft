package com.ombremoon.tennocraft.object.item.mineframe.transference;

import com.ombremoon.tennocraft.object.item.mineframe.IModHolder;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.ability.AbilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExcaliburKey extends TransferenceKeyItem implements IModHolder {
    private final Supplier<List<AbilityType<?>>> excaliburAbilities = () -> new ArrayList<>() {{
//        add(FrameAbilities.RADIAL_BLIND.get());
    }};

    public ExcaliburKey(Properties pProperties) {
        super(pProperties);
        this.frameEnergy = 1000;
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.EXCALIBUR;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return excaliburAbilities;
    }

    @Override
    public ModType getModType() {
        return ModType.MINEFRAME;
    }
}
