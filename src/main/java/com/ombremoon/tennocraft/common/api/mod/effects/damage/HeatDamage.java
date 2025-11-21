package com.ombremoon.tennocraft.common.api.mod.effects.damage;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyDamageEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record HeatDamage(AddValue value, Optional<ModConditions> conditions, ResourceLocation id) implements ModifyDamageEffect {
    public static final MapCodec<HeatDamage> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    AddValue.CODEC.fieldOf("value").forGetter(HeatDamage::value),
                    ModConditions.codec(ModContextParams.MODDED_ATTACK).optionalFieldOf("conditions").forGetter(HeatDamage::conditions),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(HeatDamage::id)
            ).apply(instance, HeatDamage::new));

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect) {
        return new HeatDamage(effect, Optional.empty(), id);
    }

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect, LootItemCondition.Builder requirements) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.empty());
        return new HeatDamage(effect, Optional.of(conditions), id);
    }

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect, LootItemCondition.Builder requirements, AuraEffect auraEffect) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.empty());
        return new HeatDamage(effect, Optional.of(conditions), id);
    }

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect, LootItemCondition.Builder requirements, int duration) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.of(duration));
        return new HeatDamage(effect, Optional.of(conditions), id);
    }

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect, AuraEffect auraEffect, int duration) {
        ModConditions conditions = new ModConditions(Optional.empty(), Optional.of(auraEffect), Optional.of(duration));
        return new HeatDamage(effect, Optional.of(conditions), id);
    }

    static HeatDamage heatDamage(ResourceLocation id, AddValue effect, LootItemCondition.Builder requirements, AuraEffect auraEffect, int duration) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.of(duration));
        return new HeatDamage(effect, Optional.of(conditions), id);
    }

    @Override
    public ResourceKey<DamageType> damageType() {
        return TCDamageTypes.HEAT;
    }

    @Override
    public MapCodec<? extends ModifyDamageEffect> codec() {
        return CODEC;
    }
}
