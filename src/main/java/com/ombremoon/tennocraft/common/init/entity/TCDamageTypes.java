package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class TCDamageTypes {
    public static final ResourceKey<DamageType> MALFUNCTION = register("malfunction");
    public static final ResourceKey<DamageType> BULLET = register("bullet");
    public static final ResourceKey<DamageType> SLASH = register("slash");

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TennoCraft.customLocation(name));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(MALFUNCTION, new DamageType("malfunction", 0.1F));
        context.register(BULLET, new DamageType("bullet", 0.1F));
        context.register(SLASH, new DamageType("slash", 0.1F));
    }
}
