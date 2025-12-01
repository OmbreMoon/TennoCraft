package com.ombremoon.tennocraft.common.api.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public record DamageValue(Holder<DamageType> damageType, float amount) {
    public static final MapCodec<DamageValue> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DamageType.CODEC.fieldOf("type").forGetter(DamageValue::damageType),
                    Codec.FLOAT.fieldOf("maxFalloff").forGetter(DamageValue::amount)
            ).apply(instance, DamageValue::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DamageValue> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public DamageValue decode(RegistryFriendlyByteBuf buffer) {
            ResourceKey<DamageType> key = ResourceKey.streamCodec(Registries.DAMAGE_TYPE).decode(buffer);
            float amount = ByteBufCodecs.FLOAT.decode(buffer);
            Holder<DamageType> holder = buffer.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
            return new DamageValue(holder, amount);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, DamageValue value) {
            ResourceKey<DamageType> key = value.damageType().unwrapKey().orElseThrow();
            ResourceKey.streamCodec(Registries.DAMAGE_TYPE).encode(buffer, key);
            ByteBufCodecs.FLOAT.encode(buffer, value.amount());
        }
    };

    public DamageValue merge(DamageValue value) {
        if (!this.damageType.is(value.damageType.getKey()))
            return null;

        return new DamageValue(this.damageType, this.amount + value.amount);
    }

    public CompoundTag save() {
        CompoundTag compoundTag = new CompoundTag();
        ResourceKey<DamageType> key = this.damageType.unwrapKey().orElseThrow();
        compoundTag.putString("Damage Type", key.location().toString());
        compoundTag.putFloat("Amount", this.amount);
        return compoundTag;
    }

    public static DamageValue load(CompoundTag compoundTag, HolderLookup.Provider registries) {
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(compoundTag.getString("Damage Type")));
        Holder<DamageType> damageType = registries.holderOrThrow(key);
        float amount = compoundTag.getFloat("Amount");
        return new DamageValue(damageType, amount);
    }

    @Override
    public String toString() {
        return this.damageType.getKey().toString() + " = " + amount;
    }
}
