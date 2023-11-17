package com.ombremoon.tennocraft.common.init.custom;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.volt.ShockAbility;
import com.ombremoon.tennocraft.player.ability.volt.SpeedAbility;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FrameAbilities {
    public static final DeferredRegister<AbilityType<?>> FRAME_ABILITY = DeferredRegister.create(TennoCraft.customLocation("abilities"), TennoCraft.MOD_ID);
    public static final Supplier<IForgeRegistry<AbilityType<?>>> REGISTRY = FRAME_ABILITY.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<AbilityType<?>> EMPTY = FRAME_ABILITY.register("empty", () -> new AbilityType<>(TennoCraft.customLocation("empty"), FrameArmorItem.FrameType.NONE, null));
    public static final RegistryObject<AbilityType<?>> SHOCK_ABILITY = FRAME_ABILITY.register("shock_ability", () -> new AbilityType<>(TennoCraft.customLocation("shock_ability"), FrameArmorItem.FrameType.VOLT, ShockAbility::new));
    public static final RegistryObject<AbilityType<?>> SPEED_ABILITY = FRAME_ABILITY.register("speed_ability", () -> new AbilityType<>(TennoCraft.customLocation("speed_ability"), FrameArmorItem.FrameType.VOLT, SpeedAbility::new));
//    public static final RegistryObject<AbilityType<?>> RADIAL_BLIND_ABILITY = FRAME_ABILITY.register("radial_blind_ability", () -> new AbilityType<>(TennoCraft.customLocation("radial_blind_ability"), FrameArmorItem.FrameType.EXCALIBUR, RadialBlindAbility::new));

    public static void register(IEventBus modEventBus) {
        FRAME_ABILITY.register(modEventBus);
    }
}
