package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.Hitscan;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.ActiveTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.HeldTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public record RangedAttackProperties(Map<TriggerType<?>, RangedAttackProperty> attacks) {
    public static final Codec<RangedAttackProperties> CODEC = Codec.pair(TriggerType.CODEC, RangedAttackProperty.CODEC).listOf()
            .xmap(pairs -> {
                Map<TriggerType<?>, RangedAttackProperty> map = new HashMap<>();
                for (var pair : pairs) {
                    map.put(pair.getFirst(), pair.getSecond());
                }
                return new RangedAttackProperties(map);
            }, properties -> {
                List<Pair<TriggerType<?>, RangedAttackProperty>> list = new ArrayList<>();
                for (var entry : properties.attacks.entrySet()) {
                    list.add(new Pair<>(entry.getKey(), entry.getValue()));
                }
                return list;
            })
            .validate(RangedAttackProperties::validate);
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<TriggerType<?>, RangedAttackProperty>> PROPERTIES_STREAM_CODEC = ByteBufCodecs.map(HashMap::new, TriggerType.STREAM_CODEC, RangedAttackProperty.STREAM_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttackProperties> STREAM_CODEC = PROPERTIES_STREAM_CODEC.map(RangedAttackProperties::new, RangedAttackProperties::attacks);

    private static DataResult<RangedAttackProperties> validate(RangedAttackProperties properties) {
        List<TriggerType<?>> heldList = new ArrayList<>();
        properties.attacks.forEach((triggerType, schema) ->  {
            ProjectileType<?> projectileType = schema.getAttack().projectileType();
            if (triggerType.is(HeldTrigger.TYPE) && !(projectileType instanceof Hitscan)) {
                heldList.add(triggerType);
            }
        });

        if (!heldList.isEmpty()) {
            return DataResult.error(() -> "Encountered non-hitscan projectile type mapped to held trigger type");
        }

        List<TriggerType<?>> activeList = new ArrayList<>();
        properties.attacks.forEach((triggerType, schema) ->  {
            ProjectileType<?> projectileType = schema.getAttack().projectileType();
            if (triggerType.is(ActiveTrigger.TYPE) && !(projectileType instanceof SolidProjectile)) {
                activeList.add(triggerType);
            }
        });

        if (!activeList.isEmpty()) {
            return DataResult.error(() -> "Encountered non-solid projectile type mapped to active trigger type");
        }

        return DataResult.success(properties);
    }

    public int size() {
        return this.attacks.size();
    }

    public boolean isEmpty() {
        return this.attacks.isEmpty();
    }

    public RangedAttackProperty getAttack(TriggerType<?> type) {
        return this.attacks.get(type);
    }

    public Set<TriggerType<?>> triggers() {
        return this.attacks.keySet();
    }
}
