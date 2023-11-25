package com.ombremoon.tennocraft.common.init.custom;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.player.attribute.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FrameAttributes {
    public static DeferredRegister<FrameAttribute> FRAME_ATTRIBUTES = DeferredRegister.create(TennoCraft.customLocation("attributes"), TennoCraft.MOD_ID);
    public static final Supplier<IForgeRegistry<FrameAttribute>> REGISTRY = FRAME_ATTRIBUTES.makeRegistry(RegistryBuilder::new);

    //FRAME ATTRIBUTES
    /**Handles maximum damage frame can take before breaking*/
    public static final RegistryObject<FrameAttribute> HEALTH = registerAttribute("health", new RangedFrameAttribute(TennoCraft.customLocation("health"), "tennocraft.frame_attribute.frame.health", 1, 1, 5));
    /**Handles damage frame can take before taking health damage. Toxin damage disregards this number*/
    public static final RegistryObject<FrameAttribute> SHIELD = registerAttribute("shield", new RangedFrameAttribute(TennoCraft.customLocation("shield"), "tennocraft.frame_attribute.frame.shield", 1, 1, 5));
    public static final RegistryObject<FrameAttribute> ARMOR = registerAttribute("armor", new RangedFrameAttribute(TennoCraft.customLocation("armor"), "tennocraft.frame_attribute.frame.armor", 1, 1, 5));
    public static final RegistryObject<FrameAttribute> ENERGY = registerAttribute("energy", new RangedFrameAttribute(TennoCraft.customLocation("energy"), "tennocraft.frame_attribute.frame.energy", 50, 0, 1000));
    public static final RegistryObject<FrameAttribute> RANK = registerAttribute("rank", new RangedFrameAttribute(TennoCraft.customLocation("rank"), "tennocraft.frame_attribute.frame.rank", 1, 1, 5));
    public static final RegistryObject<FrameAttribute> STRENGTH = registerAttribute("strength", new RangedFrameAttribute(TennoCraft.customLocation("strength"), "tennocraft.frame_attribute.frame.strength", 0, -10, 10));
    public static final RegistryObject<FrameAttribute> RANGE = registerAttribute("range", new RangedFrameAttribute(TennoCraft.customLocation("range"), "tennocraft.frame_attribute.frame.range", 0, -10, 10));
    public static final RegistryObject<FrameAttribute> EFFICIENCY = registerAttribute("efficiency", new RangedFrameAttribute(TennoCraft.customLocation("efficiency"), "tennocraft.frame_attribute.frame.efficiency", 0, -10, 10));
    public static final RegistryObject<FrameAttribute> DURATION = registerAttribute("duration", new RangedFrameAttribute(TennoCraft.customLocation("duration"), "tennocraft.frame_attribute.frame.duration", 0, -10, 10));

    //WEAPON ATTRIBUTES


    private static RegistryObject<FrameAttribute> registerAttribute(String attributeName, FrameAttribute frameAttribute) {
        return FRAME_ATTRIBUTES.register(attributeName, () -> frameAttribute);
    }

    public static void register(IEventBus modEventBus) {
        FRAME_ATTRIBUTES.register(modEventBus);
    }

    public static FrameAttribute getFrameAttribute(ResourceLocation resourceLocation) {
        return REGISTRY.get().getValue(new ResourceLocation(String.valueOf(resourceLocation)));
    }
}
