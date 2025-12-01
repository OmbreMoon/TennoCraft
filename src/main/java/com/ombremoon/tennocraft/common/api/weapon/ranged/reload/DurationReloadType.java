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

public record DurationReloadType(int initialDelay, int ammoPerSecond, Optional<ReloadType.ConsumeEvent> consumeWhen) implements ReloadType<DurationReloadType> {

    @Override
    public ReloadSerializer<DurationReloadType> getSerializer() {
        return TCReloadSerializers.DURATION;
    }

    @Override
    public int reloadTime() {
        return 0;
    }

    @Override
    public void reload(LivingEntity entity, ItemStack stack, int reloadAmount) {
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            stack.set(TCData.MAG_COUNT, stack.getOrDefault(TCData.MAG_COUNT, 0) + this.ammoPerSecond);
        }
    }

    public static class Serializer implements ReloadSerializer<DurationReloadType> {
        private static final ResourceLocation LOCATION = CommonClass.customLocation("duration");
        public static final MapCodec<DurationReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("initial_delay").forGetter(DurationReloadType::initialDelay),
                        ExtraCodecs.POSITIVE_INT.fieldOf("ammo_per_second").forGetter(DurationReloadType::ammoPerSecond),
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(DurationReloadType::consumeWhen)
                ).apply(instance, DurationReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, DurationReloadType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, DurationReloadType::initialDelay,
                ByteBufCodecs.VAR_INT, DurationReloadType::ammoPerSecond,
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), DurationReloadType::consumeWhen,
                DurationReloadType::new
        );

        @Override
        public ResourceLocation id() {
            return LOCATION;
        }

        @Override
        public MapCodec<DurationReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DurationReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
