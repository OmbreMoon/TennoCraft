package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record ModSlot(ModPolarity polarity, int index) {
    public static final MapCodec<ModSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ModPolarity.CODEC.fieldOf("polarity").forGetter(modSlot -> modSlot.polarity),
                    Codec.INT.fieldOf("index").forGetter(modSlot -> modSlot.index)
            ).apply(instance, ModSlot::new)
    );
    public static final Codec<ModSlot> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<FriendlyByteBuf, ModSlot> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(ModPolarity.class), ModSlot::polarity,
            ByteBufCodecs.VAR_INT, ModSlot::index,
            ModSlot::new
    );

    public ModSlot(ModPolarity polarity, int index) {
        this.polarity = polarity;
        this.index = index;
        if (index < 0 || index > 9)
            throw new IllegalArgumentException("Slot index must be between 0 and 9: " + index);

    }
}
