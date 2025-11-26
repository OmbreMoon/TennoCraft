package com.ombremoon.tennocraft.common.api.mod.effects.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModDamageEffect;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public record DamageModifiers(Map<ResourceLocation, Entry> modifiers) {
    public static final Codec<DamageModifiers> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Entry.CODEC).xmap(DamageModifiers::new, DamageModifiers::modifiers);

    public void put(ResourceLocation id, int modRank, ModDamageEffect effect, boolean shouldPersist) {
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

    public record Entry(ModDamageEffect effect, int modRank, boolean shouldPersist) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ModDamageEffect.CODEC.fieldOf("effect").forGetter(Entry::effect),
                        Codec.INT.fieldOf("modRank").forGetter(Entry::modRank),
                        Codec.BOOL.fieldOf("shouldPersist").forGetter(Entry::shouldPersist)
                ).apply(instance, Entry::new)
        );
    }
}
