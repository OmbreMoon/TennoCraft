package com.ombremoon.tennocraft.common.modholder.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;

import java.util.Optional;

public record RangedAttack(
        int ammoCost,
        float fireRate,
        int multiShot,
        float punchThrough,
        UniformInt spread,
        float projectileSpeed,
        ProjectileType<?> type,
        Optional<ResourceKey<DamageType>> forcedProc,
        Optional<BurstData> burst) {

    public static final Codec<RangedAttack> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ammoCost").forGetter(RangedAttack::ammoCost),
                    Codec.FLOAT.fieldOf("fireRate").forGetter(RangedAttack::fireRate),
                    Codec.INT.fieldOf("multiShot").forGetter(RangedAttack::multiShot),
                    Codec.FLOAT.fieldOf("punchThrough").forGetter(RangedAttack::punchThrough),
                    UniformInt.CODEC.fieldOf("spread").forGetter(RangedAttack::spread),
                    Codec.FLOAT.fieldOf("projectileSpeed").forGetter(RangedAttack::projectileSpeed),
                    ProjectileType.CODEC.fieldOf("projectileType").forGetter(RangedAttack::type),
                    ResourceKey.codec(Registries.DAMAGE_TYPE).optionalFieldOf("forcedProc").forGetter(RangedAttack::forcedProc),
                    BurstData.CODEC.optionalFieldOf("burst").forGetter(RangedAttack::burst)
            ).apply(instance, RangedAttack::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttack> STREAM_CODEC = StreamCodec.of(
            RangedAttack::toNetwork, RangedAttack::fromNetwork
    );

    private static RangedAttack fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ammoCost = buffer.readVarInt();
        float fireRate = buffer.readFloat();
        int multiShot = buffer.readVarInt();
        float punchThrough = buffer.readFloat();
        int minSpread = buffer.readVarInt();
        int maxSpread = buffer.readVarInt();
        float projectileSpeed = buffer.readFloat();
        ProjectileType<?> projectileType = ProjectileType.STREAM_CODEC.decode(buffer);
        Optional<ResourceKey<DamageType>> forcedProc = ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DAMAGE_TYPE)).decode(buffer);
        Optional<BurstData> data = ByteBufCodecs.optional(BurstData.STREAM_CODEC).decode(buffer);
        return new RangedAttack(ammoCost, fireRate, multiShot, punchThrough, UniformInt.of(minSpread, maxSpread), projectileSpeed, projectileType, forcedProc, data);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedAttack attack) {
        buffer.writeVarInt(attack.ammoCost);
        buffer.writeFloat(attack.fireRate);
        buffer.writeVarInt(attack.multiShot);
        buffer.writeFloat(attack.punchThrough);
        buffer.writeVarInt(attack.spread.getMinValue());
        buffer.writeVarInt(attack.spread.getMaxValue());
        buffer.writeFloat(attack.projectileSpeed);
        ProjectileType.STREAM_CODEC.encode(buffer, attack.type);
        ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DAMAGE_TYPE)).encode(buffer, attack.forcedProc);
        ByteBufCodecs.optional(BurstData.STREAM_CODEC).encode(buffer, attack.burst);
    }
}
