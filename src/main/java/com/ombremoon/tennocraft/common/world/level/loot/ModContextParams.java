package com.ombremoon.tennocraft.common.world.level.loot;

import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class ModContextParams {
    public static final LootContextParam<Integer> MOD_RANK = new LootContextParam<>(ResourceLocation.withDefaultNamespace("mod_rank"));
    public static final LootContextParam<Schema> SCHEMA = new LootContextParam<>(ResourceLocation.withDefaultNamespace("schema"));
    public static final LootContextParam<ItemStack> WEAPON = new LootContextParam<>(ResourceLocation.withDefaultNamespace("weapon"));
    public static final LootContextParam<WeaponModContainer> MODS = new LootContextParam<>(ResourceLocation.withDefaultNamespace("mods"));
    public static final LootContextParam<RangedWeaponHandler> RANGED_DATA = new LootContextParam<>(ResourceLocation.withDefaultNamespace("ranged_data"));
    public static final LootContextParam<MeleeWeaponHandler> MELEE_DATA = new LootContextParam<>(ResourceLocation.withDefaultNamespace("melee_data"));

    public static final LootContextParamSet MODDED_DAMAGE = register(
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(MOD_RANK)
                    .required(LootContextParams.ORIGIN)
                    .required(LootContextParams.DAMAGE_SOURCE)
                    .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
                    .optional(LootContextParams.ATTACKING_ENTITY)
    );

    public static final LootContextParamSet MODDED_ATTACK = register(
            builder -> builder
                    .required(MOD_RANK)
                    .required(SCHEMA)
                    .optional(LootContextParams.THIS_ENTITY)
                    .optional(LootContextParams.ORIGIN)
    );

    public static final LootContextParamSet MODDED_ITEM = register(
            builder -> builder
                    .required(WEAPON)
                    .required(MODS)
                    .optional(RANGED_DATA)
                    .optional(MELEE_DATA)
    );

    private static LootContextParamSet register(Consumer<LootContextParamSet.Builder> builderConsumer) {
        LootContextParamSet.Builder lootcontextparamset$builder = new LootContextParamSet.Builder();
        builderConsumer.accept(lootcontextparamset$builder);
        return lootcontextparamset$builder.build();
    }
}
