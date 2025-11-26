package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ModifyCritChance;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;


public record ModDamageEffect(
        Holder<DamageType> damageType,
        AddValue value,
        Optional<ModConditions> conditions,
        ResourceLocation id
) implements ModEntityEffect {
    public static final MapCodec<ModDamageEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DamageType.CODEC.fieldOf("type").forGetter(ModDamageEffect::damageType),
                    AddValue.CODEC.fieldOf("value").forGetter(ModDamageEffect::value),
                    ModConditions.codec(ModContextParams.MODDED_ATTACK).optionalFieldOf("condition").forGetter(ModDamageEffect::conditions),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModDamageEffect::id)
            ).apply(instance, ModDamageEffect::new)
    );

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, ResourceLocation id) {
        return new ModDamageEffect(damageType, value, Optional.empty(), id);
    }

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, LootItemCondition.Builder requirements, ResourceLocation id) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.empty());
        return new ModDamageEffect(damageType, value, Optional.of(conditions), id);
    }

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, LootItemCondition.Builder requirements, AuraEffect auraEffect, ResourceLocation id) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.empty());
        return new ModDamageEffect(damageType, value, Optional.of(conditions), id);
    }

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, LootItemCondition.Builder requirements, int duration, ResourceLocation id) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.of(duration));
        return new ModDamageEffect(damageType, value, Optional.of(conditions), id);
    }

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, AuraEffect auraEffect, int duration, ResourceLocation id) {
        ModConditions conditions = new ModConditions(Optional.empty(), Optional.of(auraEffect), Optional.of(duration));
        return new ModDamageEffect(damageType, value, Optional.of(conditions), id);
    }

    public static ModDamageEffect modifyDamage(Holder<DamageType> damageType, AddValue value, LootItemCondition.Builder requirements, AuraEffect auraEffect, int duration, ResourceLocation id) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.of(duration));
        return new ModDamageEffect(damageType, value, Optional.of(conditions), id);
    }

    public Integer duration() {
        return this.conditions().map(ModConditions::getDuration).orElse(0);
    }

    public Optional<AuraEffect> auraEffect() {
        return this.conditions().flatMap(ModConditions::auraEffect);
    }

    public boolean matches(LootContext context) {
        return this.conditions().map(conditions -> conditions.matches(context)).orElse(true);
    }

    @Override
    public void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 origin) {
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
    public void onDeactivated(IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, int modRank) {
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
    public MapCodec<? extends ModEntityEffect> codec() {
        return CODEC;
    }
}

