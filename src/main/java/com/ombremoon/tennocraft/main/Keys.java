package com.ombremoon.tennocraft.main;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class Keys {
    public static final ResourceKey<Registry<Schema>> SCHEMA = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("schema"));
    public static final ResourceKey<Registry<Modification>> MOD = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("mod"));
    public static final ResourceKey<Registry<ComboSet>> COMBO_SET = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("combo_set"));

    public static final LootContextParam<Integer> MOD_RANK = new LootContextParam<>(ResourceLocation.withDefaultNamespace("mod_rank"));

}
