package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.EitherCodec;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ItemModifiers;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ModifyCritChance;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModifyItemEffect extends ModEntityEffect {
    ResourceKey<Registry<MapCodec<? extends ModifyItemEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("modify_item_effect_types"));
    Registry<MapCodec<? extends ModifyItemEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModifyItemEffect>> MOD_ITEM_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<ModifyItemEffect> CODEC = REGISTRY
            .byNameCodec()
            .dispatch(ModifyItemEffect::codec, Function.identity());

    static Supplier<MapCodec<? extends ModifyItemEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModifyItemEffect>> registry) {
        registry.register("modify_crit", () -> ModifyCritChance.CODEC);
        return null;
    }

    DataComponentType<List<ConditionalModEffect<ModValueEffect>>> withComponent();

    Optional<ModConditions> conditions();

    ResourceLocation id();

    ModValueEffect value();

    default Integer duration() {
        return this.conditions().map(ModConditions::getDuration).orElse(0);
    }

    default Optional<AuraEffect> auraEffect() {
        return this.conditions().flatMap(ModConditions::auraEffect);
    }

    default boolean matches(LootContext context) {
        return this.conditions().map(conditions -> conditions.matches(context)).orElse(true);
    }

    @Override
    default void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 origin) {
        /*TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var slot : SlotGroup.values()) {
            IModHolder<?> holder = slots.get(slot);
            if (holder instanceof IWeaponModHolder<?> weaponHolder && this.matches(Modification.moddedAttackContext(level, holder.schema(stack), modRank, attacker, entity))) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                ItemModifiers modifiers = mods.getItemModifiers(this.withComponent());
                modifiers.put(this.id(), modRank, this, this.auraEffect().isPresent());
            }
        }*/
    }

    @Override
    default void onDeactivated(IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, int modRank) {
        /*TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var slot : SlotGroup.values()) {
            IModHolder<?> holder = slots.get(slot);
            if (holder instanceof IWeaponModHolder<?> weaponHolder) {
                WeaponModContainer mods = (WeaponModContainer) weaponHolder.getMods(stack);
                ItemModifiers modifiers = mods.getItemModifiers(this.withComponent());
                modifiers.remove(this.id());
            }
        }*/
    }

    @Override
    MapCodec<? extends ModifyItemEffect> codec();
}
