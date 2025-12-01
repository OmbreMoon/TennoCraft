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

public record AutoTrigger(Optional<Integer> reloadDelay, Optional<LootItemCondition> canFire) implements TriggerType<AutoTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("auto");

    public AutoTrigger(int reloadDelay, LootItemCondition canFire) {
        this(Optional.of(reloadDelay), Optional.of(canFire));
    }

    public AutoTrigger(int reloadDelay) {
        this(Optional.of(reloadDelay), Optional.empty());
    }

    public AutoTrigger() {
        this(Optional.empty(), Optional.empty());
    }

    @Override
    public TriggerSerializer<AutoTrigger> getSerializer() {
        return TCTriggerSerializers.AUTO;
    }

    @Override
    public boolean isAutoFire() {
        return true;
    }

    @Override
    public int getReloadDelay() {
        return this.reloadDelay.orElse(0);
    }

    public static class Serializer implements TriggerSerializer<AutoTrigger> {
        public static final MapCodec<AutoTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.optionalFieldOf("reload_delay").forGetter(AutoTrigger::reloadDelay),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(AutoTrigger::canFire)
                ).apply(instance, AutoTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AutoTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public AutoTrigger decode(RegistryFriendlyByteBuf buffer) {
                Optional<Integer> maxDeployables = ByteBufCodecs.optional(ByteBufCodecs.VAR_INT).decode(buffer);
                return new AutoTrigger(maxDeployables, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, AutoTrigger value) {
                ByteBufCodecs.optional(ByteBufCodecs.INT).encode(buffer, value.reloadDelay);
            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<AutoTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AutoTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
