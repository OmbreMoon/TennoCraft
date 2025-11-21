package com.ombremoon.tennocraft.common.api.mod.effects.item;

import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemEffect;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public record ItemModifiers(Map<ResourceLocation, Entry> modifiers) {

    public void put(ResourceLocation id, int modRank, ModifyItemEffect effect, boolean shouldPersist) {
        this.modifiers.put(id, new Entry(effect, modRank, shouldPersist));
    }

    public void remove(ResourceLocation id) {
        this.modifiers.remove(id);
    }

    public void forEach(BiConsumer<ResourceLocation, Entry> consumer) {
        this.modifiers.forEach(consumer);
    }

    public void removeIf(BiPredicate<ResourceLocation, Entry> removeIf) {
        for (var modifier : this.modifiers.entrySet()) {
            if (removeIf.test(modifier.getKey(), modifier.getValue())) {
                this.modifiers.remove(modifier.getKey());
            }
        }
    }

    public record Entry(ModifyItemEffect effect, int modRank, boolean shouldPersist) {}
}
