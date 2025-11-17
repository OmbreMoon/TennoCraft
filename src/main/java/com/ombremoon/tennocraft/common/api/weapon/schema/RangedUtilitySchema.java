package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.Accuracy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record RangedUtilitySchema(Accuracy accuracy, float fireRate, int maxAmmo, int magSize, int ammoPickup, float reloadTime, float rivenDisposition) {
    public static final Codec<RangedUtilitySchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Accuracy.CODEC.fieldOf("accuracy").forGetter(RangedUtilitySchema::accuracy),
                    Codec.FLOAT.fieldOf("fireRate").forGetter(RangedUtilitySchema::fireRate),
                    Codec.INT.fieldOf("maxAmmo").forGetter(RangedUtilitySchema::maxAmmo),
                    Codec.INT.fieldOf("magSize").forGetter(RangedUtilitySchema::magSize),
                    Codec.INT.fieldOf("ammoPickup").forGetter(RangedUtilitySchema::ammoPickup),
                    Codec.FLOAT.fieldOf("reloadTime").forGetter(RangedUtilitySchema::reloadTime),
                    Codec.FLOAT.fieldOf("rivenDisposition").forGetter(RangedUtilitySchema::rivenDisposition)
            ).apply(instance, RangedUtilitySchema::new)
    );
    public static final StreamCodec<FriendlyByteBuf, RangedUtilitySchema> STREAM_CODEC = StreamCodec.of(
            RangedUtilitySchema::toNetwork, RangedUtilitySchema::fromNetwork
    );

    private static RangedUtilitySchema fromNetwork(FriendlyByteBuf buffer) {
        Accuracy accuracy = buffer.readEnum(Accuracy.class);
        float fireRate = buffer.readFloat();
        int maxAmmo = buffer.readVarInt();
        int magSize = buffer.readVarInt();
        int ammoPickup = buffer.readVarInt();
        float reloadTime = buffer.readFloat();
        float rivenDisposition = buffer.readFloat();
        return new RangedUtilitySchema(accuracy, fireRate, maxAmmo, magSize, ammoPickup, reloadTime, rivenDisposition);
    }

    private static void toNetwork(FriendlyByteBuf buffer, RangedUtilitySchema schema) {
        buffer.writeEnum(schema.accuracy);
        buffer.writeFloat(schema.fireRate);
        buffer.writeVarInt(schema.maxAmmo);
        buffer.writeVarInt(schema.magSize);
        buffer.writeVarInt(schema.ammoPickup);
        buffer.writeFloat(schema.reloadTime);
        buffer.writeFloat(schema.rivenDisposition);
    }

}
