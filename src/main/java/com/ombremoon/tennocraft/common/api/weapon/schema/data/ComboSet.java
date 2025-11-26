package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.main.Keys;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.*;

public record ComboSet(Map<ComboType, List<ComboValue>> combos) {
    public static final Codec<ComboSet> DIRECT_CODEC = Codec.unboundedMap(ComboType.CODEC, ComboValue.CODEC.listOf()).xmap(ComboSet::new, ComboSet::combos)/*.validate(ComboSet::validate)*/;
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<ComboType, List<ComboValue>>> SET_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, NeoForgeStreamCodecs.enumCodec(ComboType.class), ComboValue.STREAM_CODEC.apply(ByteBufCodecs.list()));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComboSet> DIRECT_STREAM_CODEC = SET_STREAM_CODEC.map(ComboSet::new, ComboSet::combos);
    public static final Codec<Holder<ComboSet>> CODEC = RegistryFileCodec.create(Keys.COMBO_SET, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ComboSet>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Keys.COMBO_SET);

    private static DataResult<ComboSet> validate(ComboSet comboSet) {
        List<ComboType> comboTypes = new ArrayList<>(List.of(ComboType.values()));
        comboTypes.removeAll(comboSet.combos.keySet());
        return !comboTypes.isEmpty() ? DataResult.error(() -> "Missing combos for combo types: " + comboTypes) : DataResult.success(comboSet);
    }

    public static Builder define() {
        return new Builder();
    }

    public static class Builder {
        private final ImmutableMap.Builder<ComboType, List<ComboValue>> comboBuilder = ImmutableMap.builder();

        Builder() {

        }

        public Builder newCombo(ComboType type, ComboValue... combos) {
            this.comboBuilder.put(type, Arrays.asList(combos));
            return this;
        }

        public ComboSet build() {
            return new ComboSet(this.comboBuilder.build());
        }
    }
}
