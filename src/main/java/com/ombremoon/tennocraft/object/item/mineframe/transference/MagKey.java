package com.ombremoon.tennocraft.object.item.mineframe.transference;

import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.ability.AbilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MagKey extends TransferenceKeyItem implements IModHolder {
    private final Supplier<List<AbilityType<?>>> magAbilities = () -> new ArrayList<>() {{
//        add(FrameAbilities.PULL_ABILITY.get());
    }};

    public MagKey(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.MAG;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return magAbilities;
    }

    @Override
    public ModType getModType() {
        return ModType.MINEFRAME;
    }
}
