package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.NoiseLevel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;

public class AttackProperty {
    public static final Codec<AttackProperty> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    DamageValue.CODEC.codec().listOf().fieldOf("damage").forGetter(schema -> schema.damage),
                    Codec.FLOAT.fieldOf("critChance").forGetter(schema -> schema.critChance),
                    Codec.FLOAT.fieldOf("critMultiplier").forGetter(schema -> schema.critMultiplier),
                    Codec.FLOAT.fieldOf("status").forGetter(schema -> schema.status),
                    NoiseLevel.CODEC.fieldOf("noise").forGetter(schema -> schema.noise)
            ).apply(instance, AttackProperty::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AttackProperty> STREAM_CODEC = StreamCodec.of(
            AttackProperty::toNetwork, AttackProperty::fromNetwork
    );

    protected final List<DamageValue> damage = new ObjectArrayList<>();
    protected final float critChance;
    protected final float critMultiplier;
    protected final float status;
    protected final NoiseLevel noise;

    AttackProperty(List<DamageValue> damage, float critChance, float critMultiplier, float status, NoiseLevel noise) {
        this.damage.addAll(damage);
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
        this.status = status;
        this.noise = noise;
    }

    public static AttackProperty createAttack(float critChance, float critMultiplier, float status, NoiseLevel noise, DamageValue... values) {
        return new AttackProperty(Arrays.asList(values), critChance, critMultiplier, status, noise);
    }

    public List<DamageValue> getDamage() {
        return this.damage;
    }

    public float getCritChance() {
        return this.critChance;
    }

    public float getCritMultiplier() {
        return this.critMultiplier;
    }

    public float getStatus() {
        return this.status;
    }

    public NoiseLevel getNoise() {
        return this.noise;
    }

    private static AttackProperty fromNetwork(RegistryFriendlyByteBuf buffer) {
        List<DamageValue> damage = DamageValue.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        float critChance = buffer.readFloat();
        float critMultiplier = buffer.readFloat();
        float status = buffer.readFloat();
        NoiseLevel noise = buffer.readEnum(NoiseLevel.class);
        return new AttackProperty(damage, critChance, critMultiplier, status, noise);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, AttackProperty schema) {
        DamageValue.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, schema.damage);
        buffer.writeFloat(schema.critChance);
        buffer.writeFloat(schema.critMultiplier);
        buffer.writeFloat(schema.status);
        buffer.writeEnum(schema.noise);
    }
}
