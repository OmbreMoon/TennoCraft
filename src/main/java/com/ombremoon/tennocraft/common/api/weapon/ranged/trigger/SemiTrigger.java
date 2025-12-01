package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import com.ombremoon.tennocraft.common.init.TCTriggerSerializers;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record SemiTrigger(Optional<LootItemCondition> canFire) implements TriggerType<SemiTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("semi");

    public SemiTrigger(LootItemCondition canFire) {
        this(Optional.of(canFire));
    }

    public SemiTrigger() {
        this(Optional.empty());
    }

    @Override
    public TriggerSerializer<SemiTrigger> getSerializer() {
        return TCTriggerSerializers.SEMI;
    }

    public static class Serializer implements TriggerSerializer<SemiTrigger> {
        public static final MapCodec<SemiTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(SemiTrigger::canFire)
                ).apply(instance, SemiTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SemiTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public SemiTrigger decode(RegistryFriendlyByteBuf buffer) {
                return new SemiTrigger(Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, SemiTrigger value) {

            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<SemiTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SemiTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
