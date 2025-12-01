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

public record AutoSpoolTrigger(float initialFireRate, int spoolRate, Optional<Integer> initialAmmoCost, Optional<Integer> initialMultiShot, Optional<Float> initialSpread, Optional<LootItemCondition> canFire) implements TriggerType<AutoSpoolTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("auto_spool");

    @Override
    public TriggerSerializer<AutoSpoolTrigger> getSerializer() {
        return TCTriggerSerializers.AUTO_SPOOL;
    }

    @Override
    public boolean isAutoFire() {
        return true;
    }

    public float spoolFireRate(int shotsFired, float fireRate) {
        if (shotsFired >= this.spoolRate)
            return fireRate;

        float startRate = fireRate * this.initialFireRate;
        float ratePerSpool = (fireRate - startRate) / this.spoolRate;
        return startRate + ratePerSpool * shotsFired;
    }

    public int spoolAmmoCost(int shotsFired, int ammoCost) {
        if (this.initialAmmoCost.isEmpty() || shotsFired >= this.spoolRate)
            return ammoCost;

        return this.initialAmmoCost.get();
    }

    public float spoolMultiShot(int shotsFired, float multishot) {
        if (this.initialMultiShot.isEmpty() || shotsFired >= this.spoolRate)
            return multishot;

        float startMultishot = multishot * this.initialFireRate;
        float ratePerSpool = (multishot - startMultishot) / this.spoolRate;
        return startMultishot + ratePerSpool * shotsFired;
    }

    public float spoolSpread(int shotsFired, float spread) {
        if (this.initialSpread.isEmpty() || shotsFired >= this.spoolRate)
            return spread;

        float startMultishot = spread * this.initialFireRate;
        float ratePerSpool = (spread - startMultishot) / this.spoolRate;
        return startMultishot + ratePerSpool * shotsFired;
    }

    public static class Serializer implements TriggerSerializer<AutoSpoolTrigger> {
        public static final MapCodec<AutoSpoolTrigger> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("initial_fire_rate").forGetter(AutoSpoolTrigger::initialFireRate),
                        Codec.INT.fieldOf("spool_rate").forGetter(AutoSpoolTrigger::spoolRate),
                        Codec.INT.optionalFieldOf("initial_ammo").forGetter(AutoSpoolTrigger::initialAmmoCost),
                        Codec.INT.optionalFieldOf("initial_multishot").forGetter(AutoSpoolTrigger::initialAmmoCost),
                        Codec.FLOAT.optionalFieldOf("initial_spread").forGetter(AutoSpoolTrigger::initialSpread),
                        ModConditions.conditionCodec(ModContextParams.MODDED_ITEM).optionalFieldOf("can_fire").forGetter(AutoSpoolTrigger::canFire)
                ).apply(instance,AutoSpoolTrigger::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AutoSpoolTrigger> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public AutoSpoolTrigger decode(RegistryFriendlyByteBuf buffer) {
                float fireRate = buffer.readFloat();
                int spoolRate = buffer.readVarInt();
                Optional<Integer> ammo = ByteBufCodecs.optional(ByteBufCodecs.VAR_INT).decode(buffer);
                Optional<Integer> multishot = ByteBufCodecs.optional(ByteBufCodecs.VAR_INT).decode(buffer);
                Optional<Float> spread = ByteBufCodecs.optional(ByteBufCodecs.FLOAT).decode(buffer);
                return new AutoSpoolTrigger(fireRate, spoolRate, ammo, multishot, spread, Optional.empty());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, AutoSpoolTrigger value) {
                buffer.writeFloat(value.initialFireRate);
                buffer.writeVarInt(value.spoolRate);
                ByteBufCodecs.optional(ByteBufCodecs.INT).encode(buffer, value.initialAmmoCost);
                ByteBufCodecs.optional(ByteBufCodecs.INT).encode(buffer, value.initialMultiShot);
                ByteBufCodecs.optional(ByteBufCodecs.FLOAT).encode(buffer, value.initialSpread);
            }
        };

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<AutoSpoolTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AutoSpoolTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
