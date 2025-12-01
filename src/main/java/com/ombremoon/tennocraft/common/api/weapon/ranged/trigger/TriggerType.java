package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.init.TCTriggerSerializers;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

import java.util.Optional;

public interface TriggerType<T extends TriggerType<T>> {
    Logger LOGGER = Constants.LOG;
    Codec<TriggerType<?>> CODEC = TCTriggerSerializers.CODEC.dispatch(TriggerType::getSerializer, TriggerSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, TriggerType<?>> STREAM_CODEC = TCTriggerSerializers.STREAM_CODEC.dispatch(TriggerType::getSerializer, TriggerSerializer::streamCodec);

    TriggerSerializer<T> getSerializer();

    Optional<LootItemCondition> canFire();

    default boolean isAutoFire() {
        return false;
    }

    default int getReloadDelay() {
        return 0;
    }

    default boolean shouldCharge() {
        return false;
    }

    default boolean canFire(ServerLevel level, ItemStack stack, WeaponModContainer mods, RangedWeaponHandler handler) {
        return this.canFire().map(lootItemCondition -> lootItemCondition.test(Modification.rangedWeaponContext(level, stack, mods, handler))).orElse(true);
    }

    default boolean is(ResourceLocation location) {
        return this.getSerializer().id() == location;
    }

    default void save(CompoundTag compoundTag) {
        CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(LOGGER::error)
                .ifPresent(tag -> compoundTag.put("Trigger Type", tag));
    }

    static Optional<TriggerType<?>> parse(CompoundTag compoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, compoundTag.get("Trigger Type"))
                .resultOrPartial(LOGGER::error);
    }
}
