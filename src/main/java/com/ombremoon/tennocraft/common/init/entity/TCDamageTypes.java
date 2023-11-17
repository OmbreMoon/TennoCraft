package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class TCDamageTypes {
    public static final ResourceKey<DamageType> MALFUNCTION = register("malfunction");

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TennoCraft.customLocation(name));
    }

    public static void bootstrap(BootstapContext<DamageType> context) { context.register(MALFUNCTION, new DamageType("malfunction", 0.1F)); }
}
