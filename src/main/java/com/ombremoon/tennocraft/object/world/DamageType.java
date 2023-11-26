package com.ombremoon.tennocraft.object.world;

import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.player.FrameAttribute;

import java.util.function.Supplier;

public enum DamageType {
    PUNCTURE(false, null),
    IMPACT(false, null),
    SLASH(false, null),
    COLD(true, null),
    ELECTRICITY(true, null),
    HEAT(true, FrameAttributes.HEAT),
    TOXIN(true, null),
    BLAST(true, null),
    CORROSIVE(true, null),
    GAS(true, null),
    MAGNETIC(true, null),
    RADIATION(true, null),
    VIRAL(true, null);

    private final boolean isElemental;
    private final Supplier<FrameAttribute> frameAttribute;

    DamageType(boolean isElemental, Supplier<FrameAttribute> frameAttribute) {
        this.isElemental = isElemental;
        this.frameAttribute = frameAttribute;
    }

    public boolean isElemental() {
        return this.isElemental;
    }

    public Supplier<FrameAttribute> getFrameAttribute() {
        return this.frameAttribute;
    }
}
