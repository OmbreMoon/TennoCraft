package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * Mag Burst - Burst Count == 0
 * Simultaneous Burst - Delay Ticks == 0
 * Auto Burst - Auto Fire == true
 */

public record BurstTrigger(int burstCount, int delayTicks, Optional<Boolean> autoFire, Optional<Integer> reloadDelayTicks, Optional<LootItemCondition> canFire) implements TriggerType<BurstTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("burst");

    public static BurstTrigger burst(int burstCount, int delayTicks, LootItemCondition canFire) {
        return new BurstTrigger(burstCount, delayTicks, Optional.empty(), Optional.empty(), Optional.of(canFire));
    }

    public static BurstTrigger magBurst(int delayTicks, int reloadDelay, LootItemCondition canFire) {
        return new BurstTrigger(0, delayTicks, Optional.empty(), Optional.of(reloadDelay), Optional.of(canFire));
    }

    public static BurstTrigger autoBurst(int burstCount, int delayTicks, int reloadDelay, LootItemCondition canFire) {
        return new BurstTrigger(burstCount, delayTicks, Optional.of(true), Optional.of(reloadDelay), Optional.of(canFire));
    }

    public static BurstTrigger autoBurst(int burstCount, int delayTicks, LootItemCondition canFire) {
        return new BurstTrigger(burstCount, delayTicks, Optional.of(true), Optional.empty(), Optional.of(canFire));
    }

    public static BurstTrigger simultaneousBurst(LootItemCondition canFire) {
        return new BurstTrigger(0, 0, Optional.empty(), Optional.empty(), Optional.of(canFire));
    }

    public static BurstTrigger burst(int burstCount, int delayTicks) {
        return new BurstTrigger(burstCount, delayTicks, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static BurstTrigger magBurst(int delayTicks, int reloadDelay) {
        return new BurstTrigger(0, delayTicks, Optional.empty(), Optional.of(reloadDelay), Optional.empty());
    }

    public static BurstTrigger autoBurst(int burstCount, int delayTicks, int reloadDelay) {
        return new BurstTrigger(burstCount, delayTicks, Optional.of(true), Optional.of(reloadDelay), Optional.empty());
    }

    public static BurstTrigger autoBurst(int burstCount, int delayTicks) {
        return new BurstTrigger(burstCount, delayTicks, Optional.of(true), Optional.empty(), Optional.empty());
    }

    public static BurstTrigger simultaneousBurst() {
        return new BurstTrigger(0, 0, Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Override
    public TriggerSerializer<BurstTrigger> getSerializer() {
        return TCTriggerSerializers.BURST;
    }

    @Override
    public boolean isAutoFire() {
        return this.autoFire.orElse(false);
    }

    @Override
    public int getReloadDelay() {
        return this.reloadDelayTicks.orElse(0);
    }

    public static class Serializer implements TriggerSerializer<BurstTrigger> {
        public static final MapCodec<BurstTrigger> CODEC = RecordCodecBuilder.<BurstTrigger>mapCodec(
                instance -> instance.group(
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("burst_count").forGetter(BurstTrigger::burstCount),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("delay_ticks").forGetter(BurstTrigger::delayTicks),
                        Codec.BOOL.optionalFieldOf("auto_fire").forGetter(BurstTrigger::autoFire),
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("reload_delay_ticks").forGetter(BurstTrigger::reloadDelayTicks),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(BurstTrigger::canFire)
                ).apply(instance, BurstTrigger::new)
        ).validate(Serializer::validate);
        public static final StreamCodec<RegistryFriendlyByteBuf, BurstTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public BurstTrigger decode(RegistryFriendlyByteBuf buffer) {
                int burstCount = buffer.readVarInt();
                int delayTicks = buffer.readVarInt();
                Optional<Boolean> autoFire = ByteBufCodecs.optional(ByteBufCodecs.BOOL).decode(buffer);
                Optional<Integer> reloadDelay = ByteBufCodecs.optional(ByteBufCodecs.VAR_INT).decode(buffer);
                return new BurstTrigger(burstCount, delayTicks, autoFire, reloadDelay, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, BurstTrigger value) {
                buffer.writeVarInt(value.burstCount);
                buffer.writeVarInt(value.delayTicks);
                ByteBufCodecs.optional(ByteBufCodecs.BOOL).encode(buffer, value.autoFire);
                ByteBufCodecs.optional(ByteBufCodecs.VAR_INT).encode(buffer, value.reloadDelayTicks);
            }
        };

        public static DataResult<BurstTrigger> validate(BurstTrigger trigger) {
            if (trigger.burstCount * trigger.delayTicks > 20) {
                return DataResult.error(() -> "Product of burst count and delay cannot surpass 20");
            } else {
                return DataResult.success(trigger);
            }
        }

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<BurstTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BurstTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
