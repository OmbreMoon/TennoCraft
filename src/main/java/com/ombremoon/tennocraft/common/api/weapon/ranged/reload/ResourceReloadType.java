package com.ombremoon.tennocraft.common.api.weapon.ranged.reload;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import com.ombremoon.tennocraft.common.world.Resource;
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

public record ResourceReloadType(int reloadTime, Resource resource, float percentResource, Optional<ReloadType.ConsumeEvent> consumeWhen) implements ReloadType<ResourceReloadType> {

    @Override
    public ReloadSerializer<ResourceReloadType> getSerializer() {
        return TCReloadSerializers.RESOURCE;
    }

    @Override
    public void reload(LivingEntity entity, ItemStack stack, int reloadAmount) {
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            stack.set(TCData.MAG_COUNT, handler.getMagSize());
            this.resource.consume(entity, reloadAmount * this.percentResource);
        }
    }

    public static class Serializer implements ReloadSerializer<ResourceReloadType> {
        private static final ResourceLocation LOCATION = CommonClass.customLocation("resource");
        public static final MapCodec<ResourceReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("reload_time").forGetter(ResourceReloadType::reloadTime),
                        Resource.CODEC.fieldOf("resource").forGetter(ResourceReloadType::resource),
                        ExtraCodecs.POSITIVE_FLOAT.fieldOf("percent_resource").forGetter(ResourceReloadType::percentResource),
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(ResourceReloadType::consumeWhen)
                ).apply(instance, ResourceReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ResourceReloadType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, ResourceReloadType::reloadTime,
                NeoForgeStreamCodecs.enumCodec(Resource.class), ResourceReloadType::resource,
                ByteBufCodecs.FLOAT, ResourceReloadType::percentResource,
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), ResourceReloadType::consumeWhen,
                ResourceReloadType::new
        );

        @Override
        public ResourceLocation id() {
            return LOCATION;
        }

        @Override
        public MapCodec<ResourceReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ResourceReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
