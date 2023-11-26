package com.ombremoon.tennocraft.player.data;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AbilityManager {

    public static void addAbility(AbstractFrameAbility ability) {
        if (ability.level != null) {
            AbilitySavedData data = AbilitySavedData.get(ability.level);
            data.ACTIVE_ABILITIES.add(ability);
            data.setDirty();
        }
    }

    @SubscribeEvent
    public static void onLevelTick(final TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.level.isClientSide() && event.level.dimension() == Level.OVERWORLD) {
            AbilitySavedData data = AbilitySavedData.get(event.level);

            data.ACTIVE_ABILITIES.removeIf(ability -> ability.isNotActive);
            for (AbstractFrameAbility ability : data.ACTIVE_ABILITIES) {
                ability.tick();
            }
            data.setDirty();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            Player player = event.getEntity();
            AbilitySavedData data = AbilitySavedData.get(event.getEntity().level());
            for (AbstractFrameAbility ability : data.ACTIVE_ABILITIES) {
                if (ability.userID.equals(player.getUUID())) {
                    ability.endAbility();
                }
            }
            data.ACTIVE_ABILITIES.removeIf(ability -> ability.userID.equals(player.getUUID()));
        }
    }
}
