package com.ombremoon.tennocraft.player.ability;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
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
}
