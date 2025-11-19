package com.ombremoon.tennocraft.common.world.level.loot;

import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

public class ModContextParamSets {

    public static final LootContextParamSet MODDED_DAMAGE = register(
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(Keys.MOD_RANK)
                    .required(LootContextParams.ORIGIN)
                    .required(LootContextParams.DAMAGE_SOURCE)
                    .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
                    .optional(LootContextParams.ATTACKING_ENTITY)
    );

    public static final LootContextParamSet MODDED_ENTITY = register(
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(Keys.MOD_RANK)
                    .required(LootContextParams.ORIGIN)
    );

    public static final LootContextParamSet MODDED_ITEM = register(
            builder -> builder
                    .required(Keys.COMPATIBILITY)
                    .required(Keys.MOD_RANK)
    );

    private static LootContextParamSet register(Consumer<LootContextParamSet.Builder> builderConsumer) {
        LootContextParamSet.Builder lootcontextparamset$builder = new LootContextParamSet.Builder();
        builderConsumer.accept(lootcontextparamset$builder);
        return lootcontextparamset$builder.build();
    }
}
