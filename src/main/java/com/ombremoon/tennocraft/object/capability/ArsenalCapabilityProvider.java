package com.ombremoon.tennocraft.object.capability;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArsenalCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<IArsenalCapability> ARSENAL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation ARSENAL_LOCATION = TennoCraft.customLocation("arsenal");

    private final IArsenalCapability arsenalCapability = new ArsenalCapabilityHandler(2);
    private final LazyOptional<IArsenalCapability> optional = LazyOptional.of(() -> arsenalCapability);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ARSENAL_CAPABILITY.orEmpty(cap, this.optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.arsenalCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.arsenalCapability.deserializeNBT(nbt);
    }
}
