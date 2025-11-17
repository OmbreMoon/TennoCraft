package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.world.item.IModHolder;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public interface ModLocationEffect {
    ResourceKey<Registry<MapCodec<? extends ModLocationEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("mod_location_effect_types"));
    Registry<MapCodec<? extends ModLocationEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModLocationEffect>> MOD_LOCATION_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    static Supplier<MapCodec<? extends ModLocationEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModLocationEffect>> registry) {
        return registry.register("attribute", () -> ModAttributeEffect.CODEC);
    }

    void onChangedBlock(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 pos, boolean applyTransientEffects);

    default void onDeactivated(IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 pos, int modRank) {
    }

    MapCodec<? extends ModLocationEffect> codec();
}
