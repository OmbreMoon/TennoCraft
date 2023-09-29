package com.ombremoon.tennocraft.player.ability;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;

public class AbilitySavedData extends SavedData {
    public final List<AbstractFrameAbility> ACTIVE_ABILITIES = new ArrayList<>();
    public final ServerLevel level;

    public AbilitySavedData(ServerLevel level) {
        super();
        this.level = level;
    }

    public static AbilitySavedData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = level.getServer().getLevel(Level.OVERWORLD);
            DimensionDataStorage storage = serverLevel.getDataStorage();
            return storage.computeIfAbsent((nbt) -> load(serverLevel, nbt), () -> new AbilitySavedData(serverLevel), "_frame_ability");
        } else {
            throw new RuntimeException("Cannot retrieve server data from the client.");
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag abilityList = new ListTag();

        for (AbstractFrameAbility ability : ACTIVE_ABILITIES) {
            if (!ability.isNotActive && !ability.isStarting()) {
                abilityList.add(ability.save());
                System.out.println("Saved");
            }
        }

        pCompoundTag.put("abilityList", abilityList);
        TennoCraft.LOGGER.info("Successfully saved ability data");
        return pCompoundTag;
    }

    public static AbilitySavedData load (ServerLevel serverLevel, CompoundTag nbt) {
        AbilitySavedData data = new AbilitySavedData(serverLevel);
        ListTag abilityList = nbt.getList("abilityList", 10);

        for (int i = 0; i < abilityList.size(); i++) {
            CompoundTag compoundTag = abilityList.getCompound(i);
            String registryName = compoundTag.getString("registryName");
            AbilityType<?> abilityType = FrameAbilities.REGISTRY.get().getValue(new ResourceLocation(registryName));

            if (abilityType != null) {
                AbstractFrameAbility ability = abilityType.create();

                if (ability.load(compoundTag, data.level)) {
                    data.ACTIVE_ABILITIES.add(ability);
                }
                else {
                    TennoCraft.LOGGER.error("Failed to load ability: " + registryName);
                }
            } else {
                TennoCraft.LOGGER.error(String.format("Invalid ability found: %s\nFailed to load.", registryName));
            }
        }
        TennoCraft.LOGGER.info("Abilities loaded successfully");
        return data;
    }
}
