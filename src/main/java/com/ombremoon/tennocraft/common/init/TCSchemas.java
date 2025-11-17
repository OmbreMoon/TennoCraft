package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.FrameSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.SchemaSerializer;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class TCSchemas {
    public static final ResourceKey<Registry<SchemaSerializer<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("schema_types"));
    public static final Registry<SchemaSerializer<?>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    public static final DeferredRegister<SchemaSerializer<?>> SCHEMAS = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    public static final Supplier<SchemaSerializer<FrameSchema>> FRAME_SCHEMA = SCHEMAS.register("frame", FrameSchema.Serializer::new);
    public static final Supplier<SchemaSerializer<RangedWeaponSchema>> RANGED_WEAPON_SCHEMA = SCHEMAS.register("ranged_weapon", RangedWeaponSchema.Serializer::new);
    public static final Supplier<SchemaSerializer<MeleeWeaponSchema>> MELEE_WEAPON_SCHEMA = SCHEMAS.register("melee_weapon", MeleeWeaponSchema.Serializer::new);

    public static void register(IEventBus modEventBus) {
        SCHEMAS.register(modEventBus);
    }
}
