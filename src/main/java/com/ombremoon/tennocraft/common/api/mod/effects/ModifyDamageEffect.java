package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.damage.HeatDamage;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModifyDamageEffect extends ModEntityEffect {
    ResourceKey<Registry<MapCodec<? extends ModifyDamageEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("modify_damage_effect_types"));
    Registry<MapCodec<? extends ModifyDamageEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModifyDamageEffect>> MOD_DAMAGE_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<ModifyDamageEffect> CODEC = REGISTRY
            .byNameCodec()
            .dispatch(ModifyDamageEffect::codec, Function.identity());

    static Supplier<MapCodec<? extends ModifyDamageEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModifyDamageEffect>> registry) {
        registry.register("heat_damage", () -> HeatDamage.CODEC);
        return null;
    }

    ResourceKey<DamageType> damageType();

    Optional<ModConditions> conditions();

    ResourceLocation id();

    AddValue value();

    default Integer duration() {
        return this.conditions().map(ModConditions::getDuration).orElse(0);
    }

    default Optional<AuraEffect> auraEffect() {
        return this.conditions().flatMap(ModConditions::auraEffect);
    }

    default boolean matches(LootContext context) {
        return this.conditions().map(conditions -> conditions.matches(context)).orElse(true);
    }

    @Override
    default void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 origin) {
        /*TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var compatibility : this.items()) {
            IModHolder<?> holder = slots.get(compatibility.getGroup());
            if (holder instanceof IWeaponModHolder<?> weaponHolder) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                DamageModifiers modifiers = mods.getDamageModifiers(this.damageType());
                modifiers.put(this.id(), modRank, this, this.auraEffect().isPresent());
            }
        }*/
    }

    @Override
    default void onDeactivated(IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, int modRank) {
        /*TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var compatibility : this.items()) {
            IModHolder<?> holder = slots.get(compatibility.getGroup());
            if (holder instanceof IWeaponModHolder<?> weaponHolder) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                DamageModifiers modifiers = mods.getDamageModifiers(this.damageType());
                modifiers.remove(this.id());
            }
        }*/
    }

    @Override
    MapCodec<? extends ModifyDamageEffect> codec();
}
