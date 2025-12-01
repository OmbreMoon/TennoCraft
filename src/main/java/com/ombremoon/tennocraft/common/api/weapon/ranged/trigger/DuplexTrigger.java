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

public record DuplexTrigger(Optional<LootItemCondition> canFire) implements TriggerType<DuplexTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("duplex");

    public DuplexTrigger(LootItemCondition canFire) {
        this(Optional.of(canFire));
    }

    public DuplexTrigger() {
        this(Optional.empty());
    }
    
    @Override
    public TriggerSerializer<DuplexTrigger> getSerializer() {
        return TCTriggerSerializers.DUPLEX;
    }

    @Override
    public boolean shouldCharge() {
        return true;
    }

    public static class Serializer implements TriggerSerializer<DuplexTrigger> {
        public static final MapCodec<DuplexTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(DuplexTrigger::canFire)
                ).apply(instance, DuplexTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, DuplexTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public DuplexTrigger decode(RegistryFriendlyByteBuf buffer) {
                return new DuplexTrigger(Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, DuplexTrigger value) {

            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<DuplexTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DuplexTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
