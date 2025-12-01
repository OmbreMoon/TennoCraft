package com.ombremoon.tennocraft.common.api.weapon.ranged.reload;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ReloadType<T extends ReloadType<T>> {
    Codec<ReloadType<?>> CODEC = TCReloadSerializers.CODEC.dispatchStable(ReloadType::getSerializer, ReloadSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, ReloadType<?>> STREAM_CODEC = TCReloadSerializers.STREAM_CODEC.dispatch(ReloadType::getSerializer, ReloadSerializer::streamCodec);

    ReloadSerializer<T> getSerializer();

    int reloadTime();

    Optional<ConsumeEvent> consumeWhen();

    void reload(LivingEntity entity, ItemStack stack, int reloadAmount);

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
