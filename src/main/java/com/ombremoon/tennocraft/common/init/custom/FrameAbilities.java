package com.ombremoon.tennocraft.common.init.custom;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.object.custom.ability.frame.volt.ShockAbility;
import com.ombremoon.tennocraft.object.custom.ability.frame.volt.SpeedAbility;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FrameAbilities {
    public static final DeferredRegister<AbilityType<?>> FRAME_ABILITY = DeferredRegister.create(TennoCraft.customLocation("abilities"), TennoCraft.MOD_ID);
    public static final Supplier<IForgeRegistry<AbilityType<?>>> REGISTRY = FRAME_ABILITY.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<AbilityType<?>> EMPTY = FRAME_ABILITY.register("empty", () -> new AbilityType<>(TennoCraft.customLocation("empty"), TransferenceKeyItem.FrameType.NONE, null));
    public static final RegistryObject<AbilityType<?>> SHOCK_ABILITY = FRAME_ABILITY.register("shock_ability", () -> new AbilityType<>(TennoCraft.customLocation("shock_ability"), TransferenceKeyItem.FrameType.VOLT, ShockAbility::new));
    public static final RegistryObject<AbilityType<?>> SPEED_ABILITY = FRAME_ABILITY.register("speed_ability", () -> new AbilityType<>(TennoCraft.customLocation("speed_ability"), TransferenceKeyItem.FrameType.VOLT, SpeedAbility::new));
//    public static final RegistryObject<AbilityType<?>> RADIAL_BLIND_ABILITY = FRAME_ABILITY.register("radial_blind_ability", () -> new AbilityType<>(TennoCraft.customLocation("radial_blind_ability"), TransferenceKeyItem.FrameType.EXCALIBUR, RadialBlindAbility::new));
//    public static final RegistryObject<AbilityType<?>> PULL_ABILITY = FRAME_ABILITY.register("pull_ability", () -> new AbilityType<>(TennoCraft.customLocation("pull_ability"), TransferenceKeyItem.FrameType.MAG, PullAbility::new));

    public static void register(IEventBus modEventBus) {
        FRAME_ABILITY.register(modEventBus);
    }
}
