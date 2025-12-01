package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.Accuracy;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedUtilityProperties;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;

import java.util.Optional;

public record RangedAttack(
        int ammoCost,
        float fireRate,
        int multiShot,
        float punchThrough,
        UniformFloat spread,
        ReloadType<?> reloadType,
        ProjectileType<?> projectileType,
        Optional<Holder<MobEffect>> forcedProc) {

    public static final Codec<RangedAttack> CODEC = RecordCodecBuilder.<RangedAttack>create(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ammo_cost").forGetter(RangedAttack::ammoCost),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("fire_rate").forGetter(RangedAttack::fireRate),
                    ExtraCodecs.POSITIVE_INT.fieldOf("multishot").forGetter(RangedAttack::multiShot),
                    Codec.FLOAT.fieldOf("punch_through").forGetter(RangedAttack::punchThrough),
                    UniformFloat.CODEC.fieldOf("spread").forGetter(RangedAttack::spread),
                    ReloadType.CODEC.fieldOf("reload_type").forGetter(RangedAttack::reloadType),
                    ProjectileType.CODEC.fieldOf("projectile_type").forGetter(RangedAttack::projectileType),
                    MobEffect.CODEC.optionalFieldOf("forced_proc").forGetter(RangedAttack::forcedProc)
            ).apply(instance, RangedAttack::new)
    ).validate(RangedAttack::validate);
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttack> STREAM_CODEC = StreamCodec.of(
            RangedAttack::toNetwork, RangedAttack::fromNetwork
    );

    public static DataResult<RangedAttack> validate(RangedAttack attack) {
        if (attack.forcedProc.isPresent() && !(attack.forcedProc.get().is(TCTags.MobEffects.STATUS_EFFECT) && attack.forcedProc.get().value() instanceof StatusEffect)) {
            return DataResult.error(() -> "Encountered invalid forced proc");
        } else if (attack.fireRate < 1.0) {
            return DataResult.error(() -> "Ranged attack fire rate cannot be less than 1.0");
        }
        return DataResult.success(attack);
    }

    private static RangedAttack fromNetwork(RegistryFriendlyByteBuf buffer) {
        int ammoCost = buffer.readVarInt();
        float fireRate = buffer.readFloat();
        int multiShot = buffer.readVarInt();
        float punchThrough = buffer.readFloat();
        float minSpread = buffer.readFloat();
        float maxSpread = buffer.readFloat();
        ReloadType<?> reloadType = ReloadType.STREAM_CODEC.decode(buffer);
        ProjectileType<?> projectileType = ProjectileType.STREAM_CODEC.decode(buffer);
        Holder<MobEffect> effect = null;
        Optional<ResourceKey<MobEffect>> key = buffer.readOptional(buffer1 -> buffer1.readResourceKey(Registries.MOB_EFFECT));
        if (key.isPresent()) {
            effect = buffer.registryAccess().holderOrThrow(key.get());
        }

        Optional<Holder<MobEffect>> forcedProc = Optional.ofNullable(effect);
        return new RangedAttack(ammoCost, fireRate, multiShot, punchThrough, UniformFloat.of(minSpread, maxSpread), reloadType, projectileType, forcedProc);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedAttack attack) {
        buffer.writeVarInt(attack.ammoCost);
        buffer.writeFloat(attack.fireRate);
        buffer.writeVarInt(attack.multiShot);
        buffer.writeFloat(attack.punchThrough);
        buffer.writeFloat(attack.spread.getMinValue());
        buffer.writeFloat(attack.spread.getMaxValue());
        ReloadType.STREAM_CODEC.encode(buffer, attack.reloadType);
        ProjectileType.STREAM_CODEC.encode(buffer, attack.projectileType);
        buffer.writeOptional(attack.forcedProc, (buffer1, value) -> buffer1.writeResourceKey(value.unwrapKey().orElseThrow()));
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformFloat spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType,
            Holder<MobEffect> forcedProc
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.of(forcedProc));
    }

    public RangedAttack(
            int ammoCost,
            float fireRate,
            int multiShot,
            float punchThrough,
            UniformFloat spread,
            ReloadType<?> reloadType,
            ProjectileType<?> projectileType
    ) {
        this(ammoCost, fireRate, multiShot, punchThrough, spread, reloadType, projectileType, Optional.empty());
    }
}
