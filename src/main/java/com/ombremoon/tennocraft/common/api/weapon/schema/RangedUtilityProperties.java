package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ZoomAttributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record RangedUtilityProperties(
        int maxAmmo,
        int magSize,
        int ammoPickup,
        float rivenDisposition,
        Optional<ZoomAttributes> zoomAttributes
) {
    public static final Codec<RangedUtilityProperties> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("max_ammo").forGetter(RangedUtilityProperties::maxAmmo),
                    ExtraCodecs.POSITIVE_INT.fieldOf("mag_size").forGetter(RangedUtilityProperties::magSize),
                    ExtraCodecs.POSITIVE_INT.fieldOf("ammo_pickup").forGetter(RangedUtilityProperties::ammoPickup),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("riven_disposition").forGetter(RangedUtilityProperties::rivenDisposition),
                    ZoomAttributes.CODEC.optionalFieldOf("zoom").forGetter(RangedUtilityProperties::zoomAttributes)
            ).apply(instance, RangedUtilityProperties::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedUtilityProperties> STREAM_CODEC = StreamCodec.of(
            RangedUtilityProperties::toNetwork, RangedUtilityProperties::fromNetwork
    );

    private static RangedUtilityProperties fromNetwork(RegistryFriendlyByteBuf buffer) {
        int maxAmmo = buffer.readVarInt();
        int magSize = buffer.readVarInt();
        int ammoPickup = buffer.readVarInt();
        float rivenDisposition = buffer.readFloat();
        Optional<ZoomAttributes> attributes = ByteBufCodecs.optional(ZoomAttributes.STREAM_CODEC).decode(buffer);
        return new RangedUtilityProperties(maxAmmo, magSize, ammoPickup, rivenDisposition, attributes);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedUtilityProperties properties) {
        buffer.writeVarInt(properties.maxAmmo);
        buffer.writeVarInt(properties.magSize);
        buffer.writeVarInt(properties.ammoPickup);
        buffer.writeFloat(properties.rivenDisposition);
        ByteBufCodecs.optional(ZoomAttributes.STREAM_CODEC).encode(buffer, properties.zoomAttributes);
    }

    public RangedUtilityProperties(
            int maxAmmo,
            int magSize,
            int ammoPickup,
            float rivenDisposition
    ) {
        this(maxAmmo, magSize, ammoPickup, rivenDisposition, Optional.empty());
    }

}
