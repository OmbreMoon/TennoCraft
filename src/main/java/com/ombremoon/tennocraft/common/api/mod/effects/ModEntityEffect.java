package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.effects.damage.HeatDamage;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ModifyCritChance;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ModEntityEffect extends ModLocationEffect {
    ResourceKey<Registry<MapCodec<? extends ModEntityEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("mod_entity_effect_types"));
    Registry<MapCodec<? extends ModEntityEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModEntityEffect>> MOD_ENTITY_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<ModEntityEffect> CODEC = REGISTRY
            .byNameCodec()
            .dispatch(ModEntityEffect::codec, Function.identity());

    static Supplier<MapCodec<? extends ModEntityEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModEntityEffect>> registry) {
        registry.register("modify_crit", () -> ModifyCritChance.CODEC);
        registry.register("heat_damage", () -> HeatDamage.CODEC);
        return null;
    }

    void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 origin);

    default void applyAura(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 origin) {

    }

    @Override
    default void onChangedBlock(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        this.apply(level, modRank, modHolder, stack, attacker, entity, pos);
    }

    @Override
    MapCodec<? extends ModEntityEffect> codec();
}
