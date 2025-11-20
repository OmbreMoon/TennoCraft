package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;

public record ModifyCritChance(List<Modification.Compatibility> items, ModValueEffect value, Optional<LootItemCondition> requirements, Optional<Integer> duration, ResourceLocation id) implements ModifyItemEffect {
    public static final MapCodec<ModifyCritChance> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Modification.Compatibility.CODEC.listOf().fieldOf("items").forGetter(ModifyCritChance::items),
                    ModValueEffect.CODEC.fieldOf("value").forGetter(ModifyCritChance::value),
                    ConditionalModEffect.conditionCodec(ModContextParams.MODDED_ENTITY).optionalFieldOf("requirements").forGetter(ModifyCritChance::requirements),
                    Codec.INT.optionalFieldOf("duration").forGetter(ModifyCritChance::duration),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModifyCritChance::id)
            ).apply(instance, ModifyCritChance::new));

    @Override
    public DataComponentType<List<ConditionalModEffect<ModValueEffect>>> withComponent() {
        return TCModEffectComponents.CRIT_CHANCE.get();
    }

    @Override
    public MapCodec<? extends ModifyItemEffect> codec() {
        return CODEC;
    }
}
