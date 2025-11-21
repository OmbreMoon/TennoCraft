package com.ombremoon.tennocraft.common.world;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import com.ombremoon.tennocraft.common.api.FrameSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class SchemaDirectory extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = Constants.LOG;
    private final HolderLookup.Provider registries;
    private static Set<Schema> SCHEMAS = Set.of();
    public static Map<ResourceLocation, SchemaHolder<FrameSchema>> MINEFRAMES = ImmutableMap.of();
    private static Map<ResourceLocation, SchemaHolder<RangedWeaponSchema>> RANGED_WEAPONS = ImmutableMap.of();
    private static Map<ResourceLocation, SchemaHolder<MeleeWeaponSchema>> MELEE_WEAPONS = ImmutableMap.of();

    private static SchemaDirectory instance;

    public static SchemaDirectory getInstance(Level level) {
        if (instance == null) {
            instance = new SchemaDirectory(level.registryAccess());
        }
        return instance;
    }

    public SchemaDirectory(HolderLookup.Provider registries) {
        super(GSON, Registries.elementsDirPath(Keys.SCHEMA));
        this.registries = registries;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Set<Schema> schemas = new ObjectOpenHashSet<>();
        ImmutableMap.Builder<ResourceLocation, SchemaHolder<FrameSchema>> frameBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<ResourceLocation, SchemaHolder<RangedWeaponSchema>> rangedBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<ResourceLocation, SchemaHolder<MeleeWeaponSchema>> meleeBuilder = ImmutableMap.builder();

        for (var entry : object.entrySet()) {
            ResourceLocation location = entry.getKey();
            try {
                Schema schema = Schema.DIRECT_CODEC.parse(this.registries.createSerializationContext(JsonOps.INSTANCE), entry.getValue()).getOrThrow(JsonParseException::new);
                ResourceKey<Schema> schemaKey = ResourceKey.create(Keys.SCHEMA, location);
                String type = location.getPath().split("/")[0];
                switch (schema) {
                    case FrameSchema frameSchema -> frameBuilder.put(location, new SchemaHolder<>(schemaKey, frameSchema, type));
                    case RangedWeaponSchema rangedWeaponSchema -> rangedBuilder.put(location, new SchemaHolder<>(schemaKey, rangedWeaponSchema, type));
                    case MeleeWeaponSchema meleeWeaponSchema -> meleeBuilder.put(location, new SchemaHolder<>(schemaKey, meleeWeaponSchema, type));
                    default -> throw new IllegalStateException("Unexpected schema type: " + schema);
                }

                schemas.add(schema);
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                LOGGER.error("Parsing error loading schema {}", location, jsonParseException);
            }
        }

        SCHEMAS = schemas;
        MINEFRAMES = frameBuilder.build();
        RANGED_WEAPONS = rangedBuilder.build();
        MELEE_WEAPONS = meleeBuilder.build();
        LOGGER.info("Loaded {} schemas", SCHEMAS.size());
    }

    public static SchemaHolder<FrameSchema> getFrame(ResourceLocation id) {
        return MINEFRAMES.get(id);
    }

    public static SchemaHolder<FrameSchema> getFrame(ResourceKey<Schema> key) {
        return getFrame(key.location());
    }

    public static Collection<SchemaHolder<FrameSchema>> getFrames() {
        return MINEFRAMES.values();
    }

    public static SchemaHolder<RangedWeaponSchema> getRangedWeapon(ResourceLocation id) {
        return RANGED_WEAPONS.get(id);
    }

    public static SchemaHolder<RangedWeaponSchema> getRangedWeapon(ResourceKey<Schema> key) {
        return getRangedWeapon(key.location());
    }

    public static Collection<SchemaHolder<RangedWeaponSchema>> getRangedWeapons() {
        return RANGED_WEAPONS.values();
    }

    public static SchemaHolder<MeleeWeaponSchema> getMeleeWeapon(ResourceLocation id) {
        return MELEE_WEAPONS.get(id);
    }

    public static SchemaHolder<MeleeWeaponSchema> getMeleeWeapon(ResourceKey<Schema> key) {
        return getMeleeWeapon(key.location());
    }

    public static Collection<SchemaHolder<MeleeWeaponSchema>> getMeleeWeapons() {
        return MELEE_WEAPONS.values();
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        SchemaDirectory directory = new SchemaDirectory(event.getRegistryAccess());
        event.addListener(directory);
    }
}
