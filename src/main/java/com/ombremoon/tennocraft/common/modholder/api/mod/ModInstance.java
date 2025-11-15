package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.slf4j.Logger;

public record ModInstance(Holder<Modification> mod, int rank) {
    public static final Logger LOGGER = Constants.LOG;
    public static final ModInstance EMPTY = new ModInstance(Holder.direct(Modification.EMPTY), 0);

    public boolean isEmpty() {
        return this.mod.value().isEmpty();
    }

    public CompoundTag save() {
        CompoundTag compoundTag = new CompoundTag();
        Modification.CODEC
                .encodeStart(NbtOps.INSTANCE, this.mod)
                .resultOrPartial(LOGGER::error)
                .ifPresent(nbt -> compoundTag.put("Mod", nbt));
        compoundTag.putInt("Rank", this.rank);
        return compoundTag;
    }

    public static ModInstance load(CompoundTag compoundTag) {
        Holder<Modification> mod = Modification.CODEC
                .parse(NbtOps.INSTANCE, compoundTag.get("Mod"))
                .getOrThrow();
        return new ModInstance(mod, compoundTag.getInt("Rank"));
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
