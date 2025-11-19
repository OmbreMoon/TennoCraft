package com.ombremoon.tennocraft.common.api.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackSchema;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import java.util.List;

public record DamageValue(Holder<DamageType> damageType, float amount) {
    public static final MapCodec<DamageValue> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DamageType.CODEC.fieldOf("type").forGetter(DamageValue::damageType),
                    Codec.FLOAT.fieldOf("amount").forGetter(DamageValue::amount)
            ).apply(instance, DamageValue::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DamageValue> STREAM_CODEC = StreamCodec.composite(
            DamageType.STREAM_CODEC, DamageValue::damageType,
            ByteBufCodecs.FLOAT, DamageValue::amount,
            DamageValue::new
    );

}
