package com.ombremoon.tennocraft.player.ability;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class AbilityType<T extends AbstractFrameAbility> {

    private final Supplier<AbstractFrameAbility> supplier;
    private final ResourceLocation resourceLocation;

    public AbilityType(ResourceLocation resourceLocation, Supplier<AbstractFrameAbility> supplier) {
        this.supplier = supplier;
        this.resourceLocation = resourceLocation;
    }

    public AbstractFrameAbility create() {
        return this.supplier.get();
    }

    @NotNull
    public ResourceLocation getRegistryName() {
        return resourceLocation;
    }
}
