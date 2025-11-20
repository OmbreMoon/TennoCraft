package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModifyItemEffect<T extends ModValueEffect> extends ModEntityEffect {
    ResourceKey<Registry<MapCodec<? extends ModifyItemEffect<?>>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("modify_item_effect_types"));
    Registry<MapCodec<? extends ModifyItemEffect<?>>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModifyItemEffect<?>>> MOD_ITEM_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<ModifyItemEffect<?>> CODEC = REGISTRY
            .byNameCodec()
            .dispatch(ModifyItemEffect::codec, Function.identity())
            .validate(ModifyItemEffect::validate);

    private static DataResult<ModifyItemEffect<?>> validate(ModifyItemEffect<?> modifyItemValue) {
        return !modifyItemValue.items().stream().allMatch(Modification.Compatibility::isWeapon) ? DataResult.error(() -> "Encountered non-weapon mod compatibility: " + modifyItemValue.items()) : DataResult.success(modifyItemValue);
    }

    static Supplier<MapCodec<? extends ModifyItemEffect<?>>> bootstrap(DeferredRegister<MapCodec<? extends ModifyItemEffect<?>>> registry) {
        registry.register("modify_crit", () -> ModifyCritChance.CODEC);
        return null;
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, Modification.Compatibility... compatibilities) {
        return new ModifyCritChance(Arrays.asList(compatibilities), effect, Optional.empty(), Optional.empty(), id);
    }

    DataComponentType<List<ConditionalModEffect<T>>> withComponent();

    List<Modification.Compatibility> items();

    Optional<LootItemCondition> requirements();

    Optional<Integer> duration();

    ResourceLocation id();

    T value();

    @Override
    default void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 origin) {
        TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var compatibility : this.items()) {
            IModHolder<?> holder = slots.get(compatibility);
            if (holder instanceof IWeaponModHolder<?> weaponHolder) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                var modifiers = mods.getItemModifiers(this.withComponent());
                modifiers.put(this.id(), modRank, new ConditionalModEffect<>(this.value(), this.requirements(), Optional.empty(), this.duration()));
            }
        }
    }

    @Override
    default void onDeactivated(IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 pos, int modRank) {
        TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var compatibility : this.items()) {
            IModHolder<?> holder = slots.get(compatibility);
            if (holder instanceof IWeaponModHolder<?> weaponHolder) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                var modifiers = mods.getItemModifiers(this.withComponent());
                modifiers.remove(this.id());
            }
        }
    }

    @Override
    MapCodec<? extends ModifyItemEffect<?>> codec();
}
