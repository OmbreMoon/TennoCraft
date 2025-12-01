package com.ombremoon.tennocraft.client.event;

import com.ombremoon.tennocraft.client.KeyBinds;
import com.ombremoon.tennocraft.client.renderer.BulletRenderer;
import com.ombremoon.tennocraft.client.renderer.layer.PlayerFrameLayer;
import com.ombremoon.tennocraft.common.init.TCEntities;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TCEntities.BULLET_PROJECTILE.get(), BulletRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayer(EntityRenderersEvent.AddLayers event) {
        for (final var skin : event.getSkins()) {
            final LivingEntityRenderer<Player, PlayerModel<Player>> playerRenderer = event.getSkin(skin);
            if (playerRenderer == null)
                continue;

            playerRenderer.addLayer(new PlayerFrameLayer<>(playerRenderer));
        }
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinds.ALTERNATE_FIRE_BINDING);
    }

    @SubscribeEvent
    public static void registerItemModels(ModelEvent.RegisterAdditional event) {
        for (var entry : Minecraft.getInstance().getResourceManager().listResources("models/schema",
                id -> id.getNamespace().equals(Constants.MOD_ID) && id.getPath().endsWith(".json")).entrySet()) {
            var modelLocation = CommonClass.customLocation(
                    entry.getKey().getPath()
                            .replace("models/", "")
                            .replace(".json", ""));
            event.register(ModelResourceLocation.standalone(modelLocation));
        }
    }
}
