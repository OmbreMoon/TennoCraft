package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

public interface ReloadType<T extends ReloadType<T>> {
    MapCodec<ReloadType<?>> CODEC = TCReloadSerializers.REGISTRY
            .byNameCodec()
            .dispatchMap(ReloadType::getSerializer, ReloadSerializer::codec);

    StreamCodec<RegistryFriendlyByteBuf, ReloadType<?>> STREAM_CODEC = ByteBufCodecs.registry(TCReloadSerializers.RESOURCE_KEY)
            .dispatch(ReloadType::getSerializer, ReloadSerializer::streamCodec);

    ReloadSerializer<T> getSerializer();

    Optional<ConsumeEvent> consumeWhen();

    enum ConsumeEvent implements StringRepresentable {
        ON_FIRE("on_fire"),
        ON_HIT("on_hit");

        public static final Codec<ConsumeEvent> CODEC = StringRepresentable.fromEnum(ConsumeEvent::values);
        private final String name;

        ConsumeEvent(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

}
