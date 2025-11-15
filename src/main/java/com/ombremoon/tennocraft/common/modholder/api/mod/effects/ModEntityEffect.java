package com.ombremoon.tennocraft.common.modholder.api.mod.effects;

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

public interface ModEntityEffect extends ModLocationEffect {
    ResourceKey<Registry<MapCodec<? extends ModEntityEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("mod_entity_effect_types"));
    Registry<MapCodec<? extends ModEntityEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModEntityEffect>> MOD_ENTITY_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    static Supplier<MapCodec<? extends ModEntityEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModEntityEffect>> registry) {
        return null;
    }

    void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 origin);

    @Override
    default void onChangedBlock(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        this.apply(level, modRank, modHolder, stack, entity, pos);
    }

    @Override
    MapCodec<? extends ModEntityEffect> codec();
}
