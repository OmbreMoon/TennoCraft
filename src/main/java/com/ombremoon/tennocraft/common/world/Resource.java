package com.ombremoon.tennocraft.common.world;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum Resource implements StringRepresentable {
    HEALTH("heath", (entity, health) -> entity.hurt(new DamageSource(entity.registryAccess().holderOrThrow(TCDamageTypes.GENERIC)), health)),
    SHIELD("shield", (entity, health) -> {}),
    ENERGY("energy", (entity, health) -> {}),
    MAX_HEALTH("max_heath", (entity, health) -> {
        float hurtAmount = entity.getMaxHealth() * health;
        entity.hurt(new DamageSource(entity.registryAccess().holderOrThrow(TCDamageTypes.GENERIC)), hurtAmount);
    }),
    MAX_SHIELD("max_shield", (entity, health) -> {}),
    MAX_ENERGY("max_energy", (entity, health) -> {});

    public static final Codec<Resource> CODEC = StringRepresentable.fromEnum(Resource::values);
    private final String name;
    private final BiConsumer<LivingEntity, Float> consumeResource;

    Resource(String name, BiConsumer<LivingEntity, Float> consumeResource) {
        this.name = name;
        this.consumeResource = consumeResource;
    }

    public void consume(LivingEntity entity, float amount) {
        this.consumeResource.accept(entity, amount);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
