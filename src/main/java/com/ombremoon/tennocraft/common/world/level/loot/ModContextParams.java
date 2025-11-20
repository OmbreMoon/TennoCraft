package com.ombremoon.tennocraft.common.world.level.loot;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class ModContextParams {
    public static final LootContextParam<Integer> MOD_RANK = new LootContextParam<>(ResourceLocation.withDefaultNamespace("mod_rank"));
    public static final LootContextParam<Schema> SCHEMA = new LootContextParam<>(ResourceLocation.withDefaultNamespace("schema"));

    public static final LootContextParamSet MODDED_DAMAGE = register(
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(MOD_RANK)
                    .required(LootContextParams.ORIGIN)
                    .required(LootContextParams.DAMAGE_SOURCE)
                    .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
                    .optional(LootContextParams.ATTACKING_ENTITY)
    );

    public static final LootContextParamSet MODDED_ENTITY = register(
            builder -> builder
                    .required(LootContextParams.THIS_ENTITY)
                    .required(MOD_RANK)
                    .required(LootContextParams.ORIGIN)
                    .optional(SCHEMA)
    );

    public static final LootContextParamSet MODDED_ITEM = register(
            builder -> builder
                    .required(SCHEMA)
    );

    private static LootContextParamSet register(Consumer<LootContextParamSet.Builder> builderConsumer) {
        LootContextParamSet.Builder lootcontextparamset$builder = new LootContextParamSet.Builder();
        builderConsumer.accept(lootcontextparamset$builder);
        return lootcontextparamset$builder.build();
    }
}
