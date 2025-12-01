package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;
import java.util.Optional;

public record GeneralProperties(WeaponSlot slot, int mastery, int maxRank, ModLayout layout, Optional<List<ResourceLocation>> triggerTypes) {
    public static final Codec<GeneralProperties> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    WeaponSlot.CODEC.fieldOf("slot").forGetter(GeneralProperties::slot),
                    Codec.INT.fieldOf("mastery").forGetter(GeneralProperties::mastery),
                    Codec.INT.fieldOf("maxRank").forGetter(GeneralProperties::maxRank),
                    ModLayout.CODEC.fieldOf("layout").forGetter(GeneralProperties::layout),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("triggerTypes").forGetter(GeneralProperties::triggerTypes)
            ).apply(instance, GeneralProperties::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, GeneralProperties> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(WeaponSlot.class), GeneralProperties::slot,
            ByteBufCodecs.VAR_INT, GeneralProperties::mastery,
            ByteBufCodecs.VAR_INT, GeneralProperties::maxRank,
            ModLayout.STREAM_CODEC, (GeneralProperties::layout),
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list())), GeneralProperties::triggerTypes,
            GeneralProperties::new
    );
}
