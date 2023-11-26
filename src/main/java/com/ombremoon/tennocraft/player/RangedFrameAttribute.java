package com.ombremoon.tennocraft.player;

import com.ombremoon.tennocraft.player.FrameAttribute;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class RangedFrameAttribute extends FrameAttribute {
    private final float minValue;
    private final float maxValue;

    public RangedFrameAttribute(@Nullable ResourceLocation frameAttribute, String descriptionId, float defaultValue, float minValue, float maxValue) {
        super(frameAttribute, descriptionId, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        if (minValue > maxValue) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        } else if (defaultValue < minValue) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        } else if (defaultValue > maxValue) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public float getMinValue() {
        return this.minValue;
    }

    public float getMaxValue() {
        return this.maxValue;
    }
}
