package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.modholder.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.modholder.api.weapon.NoiseLevel;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.data.RangedAttack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class RangedAttackSchema extends AttackSchema {
    public static final Codec<RangedAttackSchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RangedAttack.CODEC.fieldOf("attack").forGetter(schema -> schema.attack),
                    DamageValue.CODEC.listOf().fieldOf("damage").forGetter(schema -> schema.damage),
                    Codec.FLOAT.fieldOf("critChance").forGetter(schema -> schema.critChance),
                    Codec.FLOAT.fieldOf("critMultiplier").forGetter(schema -> schema.critMultiplier),
                    Codec.FLOAT.fieldOf("status").forGetter(schema -> schema.status),
                    NoiseLevel.CODEC.fieldOf("noise").forGetter(schema -> schema.noise)
            ).apply(instance, RangedAttackSchema::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttackSchema> STREAM_CODEC = StreamCodec.of(
            RangedAttackSchema::toNetwork, RangedAttackSchema::fromNetwork
    );
    private final RangedAttack attack;

    public RangedAttackSchema(RangedAttack attack, List<DamageValue> damageValues, float critChance, float critMultiplier, float status, NoiseLevel noise) {
        super(damageValues, critChance, critMultiplier, status, noise);
        this.attack = attack;
    }

    public RangedAttack getAttack() {
        return this.attack;
    }

    private static RangedAttackSchema fromNetwork(RegistryFriendlyByteBuf buffer) {
        RangedAttack attack = RangedAttack.STREAM_CODEC.decode(buffer);
        List<DamageValue> damage = DamageValue.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        float critChance = buffer.readFloat();
        float critMultiplier = buffer.readFloat();
        float status = buffer.readFloat();
        NoiseLevel noise = buffer.readEnum(NoiseLevel.class);
        return new RangedAttackSchema(attack, damage, critChance, critMultiplier, status, noise);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedAttackSchema schema) {
        RangedAttack.STREAM_CODEC.encode(buffer, schema.attack);
        DamageValue.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, schema.damage);
        buffer.writeFloat(schema.critChance);
        buffer.writeFloat(schema.critMultiplier);
        buffer.writeFloat(schema.status);
        buffer.writeEnum(schema.noise);
    }
}
