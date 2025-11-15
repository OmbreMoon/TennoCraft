package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.modholder.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

public record GeneralSchema(WeaponSlot slot, int mastery, int maxRank, ModLayout layout, List<TriggerType> triggerTypes) {
    public static final Codec<GeneralSchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    WeaponSlot.CODEC.fieldOf("slot").forGetter(GeneralSchema::slot),
                    Codec.INT.fieldOf("mastery").forGetter(GeneralSchema::mastery),
                    Codec.INT.fieldOf("maxRank").forGetter(GeneralSchema::maxRank),
                    ModLayout.CODEC.fieldOf("layout").forGetter(GeneralSchema::layout),
                    TriggerType.CODEC.listOf().fieldOf("triggerTypes").forGetter(GeneralSchema::triggerTypes)
            ).apply(instance, GeneralSchema::new)
    );
    public static final StreamCodec<FriendlyByteBuf, GeneralSchema> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(WeaponSlot.class), GeneralSchema::slot,
            ByteBufCodecs.VAR_INT, GeneralSchema::mastery,
            ByteBufCodecs.VAR_INT, GeneralSchema::maxRank,
            ModLayout.STREAM_CODEC, (GeneralSchema::layout),
            NeoForgeStreamCodecs.enumCodec(TriggerType.class).apply(ByteBufCodecs.list()), GeneralSchema::triggerTypes,
            GeneralSchema::new
    );
}
