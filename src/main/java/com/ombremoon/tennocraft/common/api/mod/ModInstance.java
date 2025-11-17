package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public record ModInstance(Holder<Modification> mod, int rank) {
    public static final Logger LOGGER = Constants.LOG;
    public static final Codec<ModInstance> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Modification.CODEC.fieldOf("mod").forGetter(ModInstance::mod),
                    Codec.INT.fieldOf("rank").forGetter(ModInstance::rank)
            ).apply(instance, ModInstance::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ModInstance> STREAM_CODEC = StreamCodec.composite(
            Modification.STREAM_CODEC, ModInstance::mod,
            ByteBufCodecs.VAR_INT, ModInstance::rank,
            ModInstance::new
    );
    public static final ModInstance EMPTY = new ModInstance(Holder.direct(Modification.EMPTY), 0);

    public boolean isEmpty() {
        return this.mod.value().isEmpty();
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        this.mod.unwrapKey().ifPresentOrElse(
                key -> compoundTag.putString("Mod", key.location().toString()),
                () -> LOGGER.warn("Attempted to save direct holder without key")
        );
        compoundTag.putInt("Rank", this.rank);
        return compoundTag;
    }

    public static ModInstance load(CompoundTag tag, HolderLookup.Provider provider) {
        if (!tag.contains("ModInstance")) {
            return EMPTY;
        }

        CompoundTag nbt = tag.getCompound("ModInstance");
        ResourceLocation location = ResourceLocation.parse(nbt.getString("Mod"));
        ResourceKey<Modification> key = ResourceKey.create(Keys.MOD, location);
        
        return provider.lookup(Keys.MOD)
                .flatMap(lookup -> lookup.get(key))
                .map(holder -> new ModInstance(holder, nbt.getInt("Rank")))
                .orElseGet(() -> {
                    LOGGER.warn("Failed to load mod with key: {}", key.location());
                    return EMPTY;
                });
    }

    public static class Mutable {
        private final Holder<Modification> mod;
        private final int rank;

        public Mutable(ModInstance instance) {
            this.mod = instance.mod;
            this.rank = instance.rank;
        }

        public ModInstance toImmutable() {
            return new ModInstance(this.mod, this.rank);
        }
    }
}
