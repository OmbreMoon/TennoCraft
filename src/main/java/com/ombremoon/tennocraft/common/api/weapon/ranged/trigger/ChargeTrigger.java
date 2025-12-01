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
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record ChargeTrigger(int chargeTime, Type type, int releaseDelay, Optional<LootItemCondition> canFire) implements TriggerType<ChargeTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("charge");

    public ChargeTrigger(int chargeTime, Type type, int releaseDelay, LootItemCondition canFire) {
        this(chargeTime, type, releaseDelay, Optional.of(canFire));
    }

    public ChargeTrigger(int chargeTime, Type type, int releaseDelay) {
        this(chargeTime, type, releaseDelay, Optional.empty());
    }

    @Override
    public TriggerSerializer<ChargeTrigger> getSerializer() {
        return TCTriggerSerializers.CHARGE;
    }

    @Override
    public boolean shouldCharge() {
        return true;
    }

    public int getMinCharge() {
        return this.chargeTime / this.type.level;
    }

    public enum Type implements StringRepresentable {
        FULL_QUICKSHOT("full_quickShot", false, 3),
        HALF_QUICKSHOT("half_quickShot", false, 2),
        NO_QUICKSHOT("no_quickShot", false, 1),
        AUTO("auto", true, 1),
        MIN_AUTO("min_auto", true, 3);

        private static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private final String name;
        private final boolean chargesAutomatically;
        private final int level;

        Type(String name, boolean chargesAutomatically, int level) {
            this.name = name;
            this.chargesAutomatically = chargesAutomatically;
            this.level = level;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public boolean chargesAutomatically() {
            return this.chargesAutomatically;
        }

        public int getLevel() {
            return this.level;
        }
    }

    public static class Serializer implements TriggerSerializer<ChargeTrigger> {
        public static final MapCodec<ChargeTrigger> CODEC = RecordCodecBuilder.<ChargeTrigger>mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("charge_time").forGetter(ChargeTrigger::chargeTime),
                        Type.CODEC.fieldOf("quickshot").forGetter(ChargeTrigger::type),
                        Codec.INT.fieldOf("release_delay").forGetter(ChargeTrigger::releaseDelay),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(ChargeTrigger::canFire)
                ).apply(instance, ChargeTrigger::new)
        ).validate(Serializer::validate);
        public static final StreamCodec<RegistryFriendlyByteBuf, ChargeTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ChargeTrigger decode(RegistryFriendlyByteBuf buffer) {
                int chargeTime = buffer.readVarInt();
                Type type = buffer.readEnum(Type.class);
                int releaseDelay = buffer.readVarInt();
                return new ChargeTrigger(chargeTime, type, releaseDelay, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ChargeTrigger value) {
                buffer.writeVarInt(value.chargeTime);
                buffer.writeEnum(value.type);
                buffer.writeVarInt(value.releaseDelay);
            }
        };

        public static DataResult<ChargeTrigger> validate(ChargeTrigger trigger) {
            return trigger.chargeTime < trigger.type.level ? DataResult.error(() -> "Charge time cannot be less than " + trigger.type.level + " ticks for type: " + trigger.type) : DataResult.success(trigger);
        }

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<ChargeTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChargeTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
