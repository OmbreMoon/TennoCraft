package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.common.api.handler.FrameHandler;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.function.Supplier;

public class TCData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID
    );
    public static final DeferredRegister.DataComponents COMPONENT_TYPES = DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID
    );

    public static final Supplier<AttachmentType<FrameHandler>> FRAME_HANDLER = ATTACHMENT_TYPES.register(
            "frame_handler", () -> AttachmentType.serializable(FrameHandler::new).copyOnDeath().build());


    public static final Supplier<AttachmentType<TennoSlots>> TENNO_SLOTS = ATTACHMENT_TYPES.register(
            "tenno_slots", () -> AttachmentType.builder(TennoSlots::new).build());

    public static final Supplier<AttachmentType<StatusEffect.ProcEntries>> STATUS_PROCS = ATTACHMENT_TYPES.register(
            "status_procs", () -> AttachmentType.builder(() -> new StatusEffect.ProcEntries(new HashMap<>())).serialize(StatusEffect.ProcEntries.CODEC).build());

    //Components
    public static final Supplier<DataComponentType<SchemaHolder<?>>> SCHEMA = COMPONENT_TYPES.registerComponentType("schema",
            builder -> builder.persistent(SchemaHolder.CODEC).networkSynchronized(SchemaHolder.STREAM_CODEC).cacheEncoding());

    public static final Supplier<DataComponentType<RangedWeaponHandler>> RANGED_WEAPON_HANDLER = COMPONENT_TYPES.registerComponentType("ranged_weapon_handler",
            builder -> builder.persistent(RangedWeaponHandler.CODEC).networkSynchronized(RangedWeaponHandler.STREAM_CODEC));

    public static final Supplier<DataComponentType<MeleeWeaponHandler>> MELEE_WEAPON_HANDLER = COMPONENT_TYPES.registerComponentType("melee_weapon_handler",
            builder -> builder.persistent(MeleeWeaponHandler.CODEC).networkSynchronized(MeleeWeaponHandler.STREAM_CODEC));

    public static final Supplier<DataComponentType<ModInstance>> MOD = COMPONENT_TYPES.registerComponentType("mod",
            builder -> builder.persistent(ModInstance.CODEC).networkSynchronized(ModInstance.STREAM_CODEC));

    public static void register(IEventBus modEventBus) {
        TCData.ATTACHMENT_TYPES.register(modEventBus);
        TCData.COMPONENT_TYPES.register(modEventBus);
    }
}
