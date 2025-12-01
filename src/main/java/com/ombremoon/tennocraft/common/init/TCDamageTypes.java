package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TCDamageTypes {
    List<ResourceKey<DamageType>> ELEMENTAL_TYPES = new ArrayList<>();

    ResourceKey<DamageType> GENERIC = key("generic");
    ResourceKey<DamageType> IMPACT = key("impact");
    ResourceKey<DamageType> PUNCTURE = key("puncture");
    ResourceKey<DamageType> SLASH = key("slash");
    ResourceKey<DamageType> HEAT = elementalKey("heat");
    ResourceKey<DamageType> COLD = elementalKey("cold");
    ResourceKey<DamageType> ELECTRICITY = elementalKey("electricity");
    ResourceKey<DamageType> TOXIC = elementalKey("toxic");
    ResourceKey<DamageType> BLAST = elementalKey("blast");
    ResourceKey<DamageType> CORROSIVE = elementalKey("corrosive");
    ResourceKey<DamageType> GAS = elementalKey("gas");
    ResourceKey<DamageType> MAGNETIC = elementalKey("magnetic");
    ResourceKey<DamageType> RADIATION = elementalKey("radiation");
    ResourceKey<DamageType> VIRAL = elementalKey("viral");

    static void bootstrap(BootstrapContext<DamageType> context) {
        register(context, GENERIC);
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

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CommonClass.customLocation(name));
    }

    private static ResourceKey<DamageType> elementalKey(String name) {
        var key = ResourceKey.create(Registries.DAMAGE_TYPE, CommonClass.customLocation(name));
        ELEMENTAL_TYPES.add(key);
        return key;
    }

    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key) {
        context.register(key, new DamageType(key.location().getPath(), 0.1F));
    }
}
