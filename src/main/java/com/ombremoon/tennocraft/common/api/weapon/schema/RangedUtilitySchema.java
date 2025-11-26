package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.Accuracy;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.SniperInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record RangedUtilitySchema(
        float accuracy,
        float fireRate,
        int maxAmmo,
        int magSize,
        int ammoPickup,
        int reloadTime,
        float rivenDisposition,
        Optional<SniperInfo> sniperInfo
) {
    public static final Codec<RangedUtilitySchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("accuracy").forGetter(RangedUtilitySchema::accuracy),
                    Codec.FLOAT.fieldOf("fire_rate").forGetter(RangedUtilitySchema::fireRate),
                    Codec.INT.fieldOf("max_ammo").forGetter(RangedUtilitySchema::maxAmmo),
                    Codec.INT.fieldOf("mag_size").forGetter(RangedUtilitySchema::magSize),
                    Codec.INT.fieldOf("ammo_pickup").forGetter(RangedUtilitySchema::ammoPickup),
                    Codec.INT.fieldOf("reload_time").forGetter(RangedUtilitySchema::reloadTime),
                    Codec.FLOAT.fieldOf("riven_disposition").forGetter(RangedUtilitySchema::rivenDisposition),
                    SniperInfo.CODEC.optionalFieldOf("sniper_info").forGetter(RangedUtilitySchema::sniperInfo)
            ).apply(instance, RangedUtilitySchema::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedUtilitySchema> STREAM_CODEC = StreamCodec.of(
            RangedUtilitySchema::toNetwork, RangedUtilitySchema::fromNetwork
    );

    private static RangedUtilitySchema fromNetwork(RegistryFriendlyByteBuf buffer) {
        float accuracy = buffer.readFloat();
        float fireRate = buffer.readFloat();
        int maxAmmo = buffer.readVarInt();
        int magSize = buffer.readVarInt();
        int ammoPickup = buffer.readVarInt();
        int reloadTime = buffer.readVarInt();
        float rivenDisposition = buffer.readFloat();
        Optional<SniperInfo> info = ByteBufCodecs.optional(SniperInfo.STREAM_CODEC).decode(buffer);
        return new RangedUtilitySchema(accuracy, fireRate, maxAmmo, magSize, ammoPickup, reloadTime, rivenDisposition, info);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedUtilitySchema schema) {
        buffer.writeFloat(schema.accuracy);
        buffer.writeFloat(schema.fireRate);
        buffer.writeVarInt(schema.maxAmmo);
        buffer.writeVarInt(schema.magSize);
        buffer.writeVarInt(schema.ammoPickup);
        buffer.writeVarInt(schema.reloadTime);
        buffer.writeFloat(schema.rivenDisposition);
        ByteBufCodecs.optional(SniperInfo.STREAM_CODEC).encode(buffer, schema.sniperInfo);
    }

    public RangedUtilitySchema(
            float accuracy,
            float fireRate,
            int maxAmmo,
            int magSize,
            int ammoPickup,
            int reloadTime,
            float rivenDisposition
    ) {
        this(accuracy, fireRate, maxAmmo, magSize, ammoPickup, reloadTime, rivenDisposition, Optional.empty());
    }

}
