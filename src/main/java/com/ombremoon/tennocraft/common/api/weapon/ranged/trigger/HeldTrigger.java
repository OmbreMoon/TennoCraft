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

public record HeldTrigger(float initialDamage, int damageRampTicks, int decayDelay, int damageDecayTicks, Optional<LootItemCondition> canFire) implements TriggerType<HeldTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("held");

    public HeldTrigger(float initialDamage, int damageRampTicks, int decayDelay, int damageDecayTicks, LootItemCondition canFire) {
        this(initialDamage, damageRampTicks, decayDelay, damageDecayTicks, Optional.of(canFire));
    }

    public HeldTrigger(float initialDamage, int damageRampTicks, int decayDelay, int damageDecayTicks) {
        this(initialDamage, damageRampTicks, decayDelay, damageDecayTicks, Optional.empty());
    }

    @Override
    public TriggerSerializer<HeldTrigger> getSerializer() {
        return TCTriggerSerializers.HELD;
    }

    @Override
    public boolean isAutoFire() {
        return true;
    }

    public static class Serializer implements TriggerSerializer<HeldTrigger> {
        public static final MapCodec<HeldTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("initial_damage").forGetter(HeldTrigger::initialDamage),
                        Codec.INT.fieldOf("damage_ramp_ticks").forGetter(HeldTrigger::damageRampTicks),
                        Codec.INT.fieldOf("decay_delay").forGetter(HeldTrigger::decayDelay),
                        Codec.INT.fieldOf("damage_decay_ticks").forGetter(HeldTrigger::damageDecayTicks),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(HeldTrigger::canFire)
                ).apply(instance, HeldTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, HeldTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public HeldTrigger decode(RegistryFriendlyByteBuf buffer) {
                float damage = buffer.readFloat();
                int damageRamp = buffer.readVarInt();
                int decayDelay = buffer.readVarInt();
                int damageDecay = buffer.readVarInt();
                return new HeldTrigger(damage, damageRamp, decayDelay, damageDecay, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, HeldTrigger value) {
                buffer.writeFloat(value.initialDamage);
                buffer.writeVarInt(value.damageRampTicks);
                buffer.writeVarInt(value.decayDelay);
                buffer.writeVarInt(value.damageDecayTicks);
            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<HeldTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HeldTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
