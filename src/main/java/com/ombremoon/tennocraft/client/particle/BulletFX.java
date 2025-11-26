package com.ombremoon.tennocraft.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record BulletFX(
        ResourceLocation id,
        AttachmentPoint attachTo,
        Optional<Vec3> offset,
        Optional<Vec3> rotation,
        Optional<Vec3> scale,
        int delay,
        Optional<Integer> duration
) {
    public static final Codec<BulletFX> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(BulletFX::id),
                    AttachmentPoint.CODEC.fieldOf("attach_to").forGetter(BulletFX::attachTo),
                    Vec3.CODEC.optionalFieldOf("offset").forGetter(BulletFX::offset),
                    Vec3.CODEC.optionalFieldOf("rotation").forGetter(BulletFX::offset),
                    Vec3.CODEC.optionalFieldOf("scale").forGetter(BulletFX::offset),
                    ExtraCodecs.POSITIVE_INT.fieldOf("delay").forGetter(BulletFX::delay),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("duration").forGetter(BulletFX::duration)
            ).apply(instance, BulletFX::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, BulletFX> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BulletFX decode(RegistryFriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            AttachmentPoint point = buffer.readEnum(AttachmentPoint.class);
            Optional<Vec3> offset = buffer.readOptional(FriendlyByteBuf::readVec3);
            Optional<Vec3> rotation = buffer.readOptional(FriendlyByteBuf::readVec3);
            Optional<Vec3> scale = buffer.readOptional(FriendlyByteBuf::readVec3);
            int delay = buffer.readVarInt();
            Optional<Integer> duration = buffer.readOptional(FriendlyByteBuf::readVarInt);
            return new BulletFX(id, point, offset, rotation, scale, delay, duration);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, BulletFX value) {
            buffer.writeResourceLocation(value.id);
            buffer.writeEnum(value.attachTo);
            buffer.writeOptional(value.offset, FriendlyByteBuf::writeVec3);
            buffer.writeOptional(value.rotation, FriendlyByteBuf::writeVec3);
            buffer.writeOptional(value.scale, FriendlyByteBuf::writeVec3);
            buffer.writeVarInt(value.delay);
            buffer.writeOptional(value.duration, FriendlyByteBuf::writeVarInt);
        }
    };

    public enum AttachmentPoint implements StringRepresentable {
        WEAPON("weapon"),
        BULLET("bullet"),
        IMPACT("impact"),
        TARGET("target");

        public static final Codec<AttachmentPoint> CODEC = StringRepresentable.fromEnum(AttachmentPoint::values);
        private final String name;

        AttachmentPoint(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
