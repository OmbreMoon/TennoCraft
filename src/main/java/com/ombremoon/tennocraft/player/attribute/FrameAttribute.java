package com.ombremoon.tennocraft.player.attribute;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FrameAttribute {

    private final ResourceLocation frameAttribute;
    private final String descriptionId;
    private final float defaultValue;

    public FrameAttribute(@Nullable ResourceLocation frameAttribute, String descriptionId, float defaultValue) {
        this.frameAttribute = frameAttribute;
        this.descriptionId = descriptionId;
        this.defaultValue = defaultValue;
    }

    public ResourceLocation getResourceLocation() {
        return this.frameAttribute;
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }
}
