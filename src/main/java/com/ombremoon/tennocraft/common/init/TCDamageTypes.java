package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public interface TCDamageTypes {
    ResourceKey<DamageType> IMPACT = register("impact");
    ResourceKey<DamageType> PUNCTURE = register("puncture");
    ResourceKey<DamageType> SLASH = register("slash");
    ResourceKey<DamageType> HEAT = register("heat");
    ResourceKey<DamageType> COLD = register("cold");
    ResourceKey<DamageType> ELECTRICITY = register("electricity");
    ResourceKey<DamageType> TOXIC = register("toxic");
    ResourceKey<DamageType> BLAST = register("blast");
    ResourceKey<DamageType> CORROSIVE = register("corrosive");
    ResourceKey<DamageType> GAS = register("gas");
    ResourceKey<DamageType> MAGNETIC = register("magnetic");
    ResourceKey<DamageType> RADIATION = register("radiation");
    ResourceKey<DamageType> VIRAL = register("viral");

    static void bootstrap(BootstrapContext<DamageType> context) {
        register(context, IMPACT);
        register(context, PUNCTURE);
        register(context, SLASH);
        register(context, HEAT);
        register(context, COLD);
        register(context, ELECTRICITY);
        register(context, TOXIC);
        register(context, BLAST);
        register(context, CORROSIVE);
        register(context, GAS);
        register(context, MAGNETIC);
        register(context, RADIATION);
        register(context, VIRAL);
    }

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CommonClass.customLocation(name));
    }

    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key) {
        context.register(key, new DamageType(key.location().getPath(), 0.1F));
    }
}
