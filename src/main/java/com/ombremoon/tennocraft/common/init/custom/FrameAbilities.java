package com.ombremoon.tennocraft.common.init.custom;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import com.ombremoon.tennocraft.player.ability.excalibur.RadialBlindAbility;
import com.ombremoon.tennocraft.player.ability.volt.SpeedAbility;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import java.util.function.Supplier;

public class FrameAbilities {
    public static final DeferredRegister<AbilityType<?>> FRAME_ABILITY = DeferredRegister.create(TennoCraft.customLocation("abilities"), TennoCraft.MOD_ID);
    public static final Supplier<IForgeRegistry<AbilityType<?>>> REGISTRY = FRAME_ABILITY.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<AbilityType<?>> SPEED_ABILITY = FRAME_ABILITY.register("speed_ability", () -> new AbilityType<>(TennoCraft.customLocation("speed_ability"), SpeedAbility::new));
    public static final RegistryObject<AbilityType<?>> RADIAL_BLIND_ABILITY = FRAME_ABILITY.register("radial_blind_ability", () -> new AbilityType<>(TennoCraft.customLocation("radial_blind_ability"), RadialBlindAbility::new));

    public static AbstractFrameAbility getByName(ResourceLocation location) {
        for (RegistryObject<AbilityType<?>> abilityType : FRAME_ABILITY.getEntries()) {
            if (abilityType.getId().equals(location)) {
                return abilityType.get().create();
            }
        }
        return null;
    }

    public static void register(IEventBus modEventBus) {
        FRAME_ABILITY.register(modEventBus);
    }
}
