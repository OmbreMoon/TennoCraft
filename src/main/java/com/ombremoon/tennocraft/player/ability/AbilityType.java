package com.ombremoon.tennocraft.player.ability;

import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AbilityType<T extends AbstractFrameAbility> {

    private final Supplier<AbstractFrameAbility> supplier;
    private final FrameArmorItem.FrameType frameType;
    private final ResourceLocation resourceLocation;

    public AbilityType(ResourceLocation resourceLocation, FrameArmorItem.FrameType frameType, Supplier<AbstractFrameAbility> supplier) {
        this.supplier = supplier;
        this.resourceLocation = resourceLocation;
        this.frameType = frameType;
    }

    public AbstractFrameAbility create() {
        return this.supplier.get();
    }

    @NotNull
    public ResourceLocation getRegistryName() {
        return resourceLocation;
    }

    public FrameArmorItem.FrameType getFrameType() {
        return this.frameType;
    }
}
