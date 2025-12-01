package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import com.ombremoon.tennocraft.common.init.TCTriggerSerializers;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record ActiveTrigger(int maxDeployables, Optional<LootItemCondition> canFire) implements TriggerType<ActiveTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("active");

    public ActiveTrigger(int maxDeployables, LootItemCondition canFire) {
        this(maxDeployables, Optional.of(canFire));
    }

    public ActiveTrigger(int maxDeployables) {
        this(maxDeployables, Optional.empty());
    }
    
    @Override
    public TriggerSerializer<ActiveTrigger> getSerializer() {
        return TCTriggerSerializers.ACTIVE;
    }

    public static class Serializer implements TriggerSerializer<ActiveTrigger> {
        public static final MapCodec<ActiveTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.fieldOf("max_deployables").forGetter(ActiveTrigger::maxDeployables),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(ActiveTrigger::canFire)
                ).apply(instance, ActiveTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ActiveTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ActiveTrigger decode(RegistryFriendlyByteBuf buffer) {
                int maxDeployables = buffer.readVarInt();
                return new ActiveTrigger(maxDeployables, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ActiveTrigger value) {
                buffer.writeVarInt(value.maxDeployables);
            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<ActiveTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ActiveTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
