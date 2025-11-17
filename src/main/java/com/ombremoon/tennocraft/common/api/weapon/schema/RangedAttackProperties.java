package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RangedAttackProperties(Map<TriggerType, List<RangedAttackSchema>> attacks) {
    public static final Codec<RangedAttackProperties> CODEC = Codec.unboundedMap(TriggerType.CODEC, RangedAttackSchema.CODEC.listOf()).xmap(RangedAttackProperties::new, RangedAttackProperties::attacks);
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<TriggerType, List<RangedAttackSchema>>> PROPERTIES_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, NeoForgeStreamCodecs.enumCodec(TriggerType.class), RangedAttackSchema.STREAM_CODEC.apply(ByteBufCodecs.list()));
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedAttackProperties> STREAM_CODEC = PROPERTIES_STREAM_CODEC.map(RangedAttackProperties::new, RangedAttackProperties::attacks);

    public int size() {
        return this.attacks.size();
    }

    public boolean isEmpty() {
        return this.attacks.isEmpty();
    }

    public void addProperty(TriggerType type, List<RangedAttackSchema> attacks) {
        this.attacks.put(type, attacks);
    }

    public List<RangedAttackSchema> getAttacks(TriggerType type) {
        return this.attacks.get(type);
    }

    public Set<TriggerType> triggers() {
        return this.attacks.keySet();
    }
}
