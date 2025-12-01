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

public record AmmoReloadType(int reloadTime, Optional<ReloadType.ConsumeEvent> consumeWhen) implements ReloadType<AmmoReloadType> {
    private static final ResourceLocation TYPE = CommonClass.customLocation("ammo");

    public AmmoReloadType(int reloadTime) {
        this(reloadTime, Optional.of(ConsumeEvent.ON_FIRE));
    }

    @Override
    public ReloadSerializer<AmmoReloadType> getSerializer() {
        return TCReloadSerializers.AMMO;
    }

    @Override
    public void reload(LivingEntity entity, ItemStack stack, int reloadAmount) {
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            stack.set(TCData.MAG_COUNT, reloadAmount);
            stack.set(TCData.AMMO_COUNT, Math.max(0, stack.getOrDefault(TCData.AMMO_COUNT, 0) - reloadAmount));
        }
    }

    public static class Serializer implements ReloadSerializer<AmmoReloadType> {
        public static final MapCodec<AmmoReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("reload_time").forGetter(AmmoReloadType::reloadTime),
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(AmmoReloadType::consumeWhen)
                ).apply(instance, AmmoReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AmmoReloadType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, AmmoReloadType::reloadTime,
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), AmmoReloadType::consumeWhen,
                AmmoReloadType::new
        );

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<AmmoReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AmmoReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
