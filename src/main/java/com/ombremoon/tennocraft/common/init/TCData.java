package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.modholder.FrameHandler;
import com.ombremoon.tennocraft.common.modholder.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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

    //Components
    public static final Supplier<DataComponentType<SchemaHolder>> SCHEMA = COMPONENT_TYPES.registerComponentType("schema",
            builder -> builder.persistent(SchemaHolder.CODEC).networkSynchronized(SchemaHolder.STREAM_CODEC).cacheEncoding());

    public static final Supplier<DataComponentType<RangedWeaponHandler>> RANGED_WEAPON_HANDLER = COMPONENT_TYPES.registerComponentType("ranged_weapon_handler",
            builder -> builder.persistent(RangedWeaponHandler.CODEC));

    public static final Supplier<DataComponentType<Holder<Modification>>> MOD = COMPONENT_TYPES.registerComponentType("mod",
            builder -> builder.persistent(Modification.CODEC).networkSynchronized(Modification.STREAM_CODEC));

    public static void register(IEventBus modEventBus) {
        TCData.ATTACHMENT_TYPES.register(modEventBus);
        TCData.COMPONENT_TYPES.register(modEventBus);
    }
}
