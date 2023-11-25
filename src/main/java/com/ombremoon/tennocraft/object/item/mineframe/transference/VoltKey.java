package com.ombremoon.tennocraft.object.item.mineframe.transference;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.ability.AbilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class VoltKey extends TransferenceKeyItem implements IModHolder {
    private final Supplier<List<AbilityType<?>>> voltAbilities = () -> new ArrayList<>() {{
        add(FrameAbilities.SHOCK_ABILITY.get());
        add(FrameAbilities.SPEED_ABILITY.get());
    }};

    public VoltKey(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.VOLT;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return voltAbilities;
    }

    @Override
    public ModType getModType() {
        return ModType.MINEFRAME;
    }
}
