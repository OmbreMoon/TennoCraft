package com.ombremoon.tennocraft.main;

import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemEffect;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCAbilities;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.RankBasedValue;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import com.ombremoon.tennocraft.common.api.mod.effects.ModEntityEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModLocationEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(Constants.MOD_ID)
public class TennoCraft {

    //Holder -> ResourceKey -- DamageValue

    public TennoCraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
        event.dataPackRegistry(Keys.SCHEMA, Schema.DIRECT_CODEC, Schema.DIRECT_CODEC);
        event.dataPackRegistry(Keys.MOD, Modification.DIRECT_CODEC, Modification.DIRECT_CODEC);
        event.dataPackRegistry(Keys.COMBO_SET, ComboSet.DIRECT_CODEC, ComboSet.DIRECT_CODEC);
    });
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerRegistry);
        CommonClass.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void registerRegistry(NewRegistryEvent event) {
        event.register(ModEntityEffect.REGISTRY);
        event.register(ModLocationEffect.REGISTRY);
        event.register(ModValueEffect.REGISTRY);
        event.register(ModifyItemEffect.REGISTRY);
        event.register(RankBasedValue.REGISTRY);
        event.register(TCAbilities.REGISTRY);
        event.register(TCModEffectComponents.REGISTRY);
        event.register(TCProjectileSerializers.REGISTRY);
        event.register(TCSchemas.REGISTRY);
    }
}
