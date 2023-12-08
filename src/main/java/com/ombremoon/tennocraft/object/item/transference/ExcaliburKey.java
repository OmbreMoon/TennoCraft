package com.ombremoon.tennocraft.object.item.transference;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.data.AbilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExcaliburKey extends TransferenceKeyItem implements IModHolder {
    private final Supplier<List<AbilityType<?>>> excaliburAbilities = () -> new ArrayList<>() {{
        add(FrameAbilities.SLASH_DASH_ABILITY.get());
        add(FrameAbilities.RADIAL_BLIND_ABILITY.get());
        add(FrameAbilities.RADIAL_JAVELIN_ABILITY.get());
        add(FrameAbilities.EXALTED_BLADE_ABILITY.get());
    }};

    public ExcaliburKey(Properties pProperties) {
        super(pProperties);
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
