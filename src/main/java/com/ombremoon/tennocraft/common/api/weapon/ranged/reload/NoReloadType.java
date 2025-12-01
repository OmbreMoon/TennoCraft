package com.ombremoon.tennocraft.common.api.weapon.ranged.reload;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record NoReloadType() implements ReloadType<NoReloadType> {
    private static final ResourceLocation TYPE = CommonClass.customLocation("no_reload");

    @Override
    public ReloadSerializer<NoReloadType> getSerializer() {
        return TCReloadSerializers.NO_RELOAD;
    }

    @Override
    public int reloadTime() {
        return 0;
    }

    @Override
    public Optional<ConsumeEvent> consumeWhen() {
        return Optional.empty();
    }

    @Override
    public void reload(LivingEntity entity, ItemStack stack, int reloadAmount) {
    }

    public static class Serializer implements ReloadSerializer<NoReloadType> {
        public static final MapCodec<NoReloadType> CODEC = MapCodec.unit(NoReloadType::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, NoReloadType> STREAM_CODEC = StreamCodec.unit(new NoReloadType());

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<NoReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NoReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
