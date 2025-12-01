package com.ombremoon.tennocraft.main;

import com.ombremoon.tennocraft.common.api.mod.effects.*;
import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.*;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.RankBasedValue;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(Constants.MOD_ID)
public class TennoCraft {

    public TennoCraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
        event.dataPackRegistry(Keys.SCHEMA, Schema.DIRECT_CODEC, Schema.DIRECT_CODEC);
        event.dataPackRegistry(Keys.MOD, Modification.DIRECT_CODEC, Modification.DIRECT_CODEC);
        event.dataPackRegistry(Keys.COMBO_SET, ComboSet.DIRECT_CODEC, ComboSet.DIRECT_CODEC);
        event.dataPackRegistry(Keys.BULLET, Bullet.DIRECT_CODEC, Bullet.DIRECT_CODEC);
    });
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerRegistry);
        CommonClass.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void registerRegistry(NewRegistryEvent event) {
        event.register(TCModEffectComponents.REGISTRY);
        event.register(ModEntityEffect.ENTITY_REGISTRY);
        event.register(ModLocationEffect.LOCATION_REGISTRY);
        event.register(ModifyItemEffect.ITEM_REGISTRY);
        event.register(ModValueEffect.REGISTRY);
        event.register(RankBasedValue.REGISTRY);
        event.register(TCAbilities.REGISTRY);
        event.register(TCSchemas.REGISTRY);
    }
}
