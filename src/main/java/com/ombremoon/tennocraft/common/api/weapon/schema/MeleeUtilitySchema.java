package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MeleeUtilitySchema(float attackSpeed, int blockAngle, int comboDuration, float rivenDisposition, float followThrough) {
    public static final Codec<MeleeUtilitySchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("attackSpeed").forGetter(MeleeUtilitySchema::attackSpeed),
                    Codec.INT.fieldOf("blockAngle").forGetter(MeleeUtilitySchema::blockAngle),
                    Codec.INT.fieldOf("comboDuration").forGetter(MeleeUtilitySchema::comboDuration),
                    Codec.FLOAT.fieldOf("rivenDisposition").forGetter(MeleeUtilitySchema::rivenDisposition),
                    Codec.FLOAT.fieldOf("followThrough").forGetter(MeleeUtilitySchema::followThrough)
            ).apply(instance, MeleeUtilitySchema::new)
    );
    public static final StreamCodec<FriendlyByteBuf, MeleeUtilitySchema> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MeleeUtilitySchema::attackSpeed,
            ByteBufCodecs.INT, MeleeUtilitySchema::blockAngle,
            ByteBufCodecs.INT, MeleeUtilitySchema::comboDuration,
            ByteBufCodecs.FLOAT, MeleeUtilitySchema::rivenDisposition,
            ByteBufCodecs.FLOAT, MeleeUtilitySchema::followThrough,
            MeleeUtilitySchema::new
    );
}
