package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.projectile.ReloadType;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;

import java.util.Optional;

public record RangedAttack(
        int ammoCost,
        float fireRate,
        int multiShot,
        float punchThrough,
        UniformInt spread,
        ReloadType<?> reloadType,
        ProjectileType<?> projectileType,
        Optional<Holder<MobEffect>> forcedProc,
        Optional<BurstData> burst) {

    public static final Codec<RangedAttack> CODEC = RecordCodecBuilder.<RangedAttack>create(
            instance -> instance.group(
                    Codec.INT.fieldOf("ammo_cost").forGetter(RangedAttack::ammoCost),
                    Codec.FLOAT.fieldOf("fire_rate").forGetter(RangedAttack::fireRate),
                    Codec.INT.fieldOf("multishot").forGetter(RangedAttack::multiShot),
                    Codec.FLOAT.fieldOf("punch_through").forGetter(RangedAttack::punchThrough),
                    UniformInt.CODEC.fieldOf("spread").forGetter(RangedAttack::spread),
                    ReloadType.CODEC.codec().fieldOf("reload_type").forGetter(RangedAttack::reloadType),
                    ProjectileType.CODEC.fieldOf("projectile_type").forGetter(RangedAttack::projectileType),
                    MobEffect.CODEC.optionalFieldOf("forced_proc").forGetter(RangedAttack::forcedProc),
                    BurstData.CODEC.optionalFieldOf("burst").forGetter(RangedAttack::burst)
            ).apply(instance, RangedAttack::new)
    ).validate(RangedAttack::validate);
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttack> STREAM_CODEC = StreamCodec.of(
            RangedAttack::toNetwork, RangedAttack::fromNetwork
    );

    public static DataResult<RangedAttack> validate(RangedAttack attack) {
        return attack.forcedProc.isPresent() && !(attack.forcedProc.get().is(TCTags.MobEffects.STATUS_EFFECT) && attack.forcedProc.get().value() instanceof StatusEffect) ? DataResult.error(() -> "Encountered invalid forced proc") : DataResult.success(attack);
    }

    private static RangedAttack fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ammoCost = buffer.readVarInt();
        float fireRate = buffer.readFloat();
        int multiShot = buffer.readVarInt();
        float punchThrough = buffer.readFloat();
        int minSpread = buffer.readVarInt();
        int maxSpread = buffer.readVarInt();
        ReloadType<?> reloadType = ReloadType.STREAM_CODEC.decode(buffer);
        ProjectileType<?> projectileType = ProjectileType.STREAM_CODEC.decode(buffer);
        Holder<MobEffect> effect = null;
        Optional<ResourceKey<MobEffect>> key = buffer.readOptional(buffer1 -> buffer1.readResourceKey(Registries.MOB_EFFECT));
        if (key.isPresent()) {
            effect = buffer.registryAccess().holderOrThrow(key.get());
        }

        Optional<Holder<MobEffect>> forcedProc = Optional.ofNullable(effect);
        Optional<BurstData> data = ByteBufCodecs.optional(BurstData.STREAM_CODEC).decode(buffer);
        return new RangedAttack(ammoCost, fireRate, multiShot, punchThrough, UniformInt.of(minSpread, maxSpread), reloadType, projectileType, forcedProc, data);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedAttack attack) {
        buffer.writeVarInt(attack.ammoCost);
        buffer.writeFloat(attack.fireRate);
        buffer.writeVarInt(attack.multiShot);
        buffer.writeFloat(attack.punchThrough);
        buffer.writeVarInt(attack.spread.getMinValue());
        buffer.writeVarInt(attack.spread.getMaxValue());
        ReloadType.STREAM_CODEC.encode(buffer, attack.reloadType);
        ProjectileType.STREAM_CODEC.encode(buffer, attack.projectileType);
        buffer.writeOptional(attack.forcedProc, (buffer1, value) -> buffer1.writeResourceKey(value.unwrapKey().orElseThrow()));
        ByteBufCodecs.optional(BurstData.STREAM_CODEC).encode(buffer, attack.burst);
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformInt spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType,
            Holder<MobEffect> forcedProc,
            BurstData burst
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.of(forcedProc), Optional.of(burst));
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformInt spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType,
            Holder<MobEffect> forcedProc
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.of(forcedProc), Optional.empty());
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformInt spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType,
            BurstData burst
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.empty(), Optional.of(burst));
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformInt spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.empty(), Optional.empty());
    }
}
