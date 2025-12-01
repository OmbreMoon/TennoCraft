package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MeleeUtilityProperties(float attackSpeed, int blockAngle, int comboDuration, float rivenDisposition, float followThrough) {
    public static final Codec<MeleeUtilityProperties> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("attackSpeed").forGetter(MeleeUtilityProperties::attackSpeed),
                    Codec.INT.fieldOf("blockAngle").forGetter(MeleeUtilityProperties::blockAngle),
                    Codec.INT.fieldOf("comboDuration").forGetter(MeleeUtilityProperties::comboDuration),
                    Codec.FLOAT.fieldOf("rivenDisposition").forGetter(MeleeUtilityProperties::rivenDisposition),
                    Codec.FLOAT.fieldOf("followThrough").forGetter(MeleeUtilityProperties::followThrough)
            ).apply(instance, MeleeUtilityProperties::new)
    );
    public static final StreamCodec<FriendlyByteBuf, MeleeUtilityProperties> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MeleeUtilityProperties::attackSpeed,
            ByteBufCodecs.INT, MeleeUtilityProperties::blockAngle,
            ByteBufCodecs.INT, MeleeUtilityProperties::comboDuration,
            ByteBufCodecs.FLOAT, MeleeUtilityProperties::rivenDisposition,
            ByteBufCodecs.FLOAT, MeleeUtilityProperties::followThrough,
            MeleeUtilityProperties::new
    );
}
