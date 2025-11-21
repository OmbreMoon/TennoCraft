package com.ombremoon.tennocraft.common.api.mod.effects.damage;

import com.ombremoon.tennocraft.common.api.mod.effects.ModifyDamageEffect;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public record DamageModifiers(Map<ResourceLocation, Entry> modifiers) {

    public void put(ResourceLocation id, int modRank, ModifyDamageEffect effect, boolean shouldPersist) {
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

    public record Entry(ModifyDamageEffect effect, int modRank, boolean shouldPersist) {}
}
