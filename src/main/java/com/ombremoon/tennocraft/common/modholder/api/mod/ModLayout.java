package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ModLayout(Modification.Compatibility compatibility, List<ModSlot> slots) {
    public static final Codec<ModLayout> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Modification.Compatibility.CODEC.fieldOf("compatibility").forGetter(ModLayout::compatibility),
                    ModSlot.CODEC.listOf().fieldOf("slots").forGetter(ModLayout::slots)
            ).apply(instance, ModLayout::new)
    );
    public static final StreamCodec<FriendlyByteBuf, ModLayout> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(Modification.Compatibility.class), ModLayout::compatibility,
            ModSlot.STREAM_CODEC.apply(ByteBufCodecs.list()), ModLayout::slots,
            ModLayout::new
    );

    public ModLayout(Modification.Compatibility compatibility, List<ModSlot> slots) {
        this.slots = slots;
        this.compatibility = compatibility;

        if (this.slots.size() > this.compatibility.getMaxSlots())
            throw new IllegalStateException("Invalid layout. Mod compatibility " + compatibility + " has " + compatibility.getMaxSlots() + " max slots: " + slots.size());

        Set<Integer> modSlots = slots.stream().map(ModSlot::index).collect(Collectors.toSet());
        if (slots.size() != modSlots.size())
            throw new IllegalStateException("Invalid layout. Mod slots with duplicate indices detected");
    }
}
