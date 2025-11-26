package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.projectile.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.world.PlayerCombo;
import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.common.api.handler.FrameHandler;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

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
            "status_procs", () -> AttachmentType.builder(() -> new StatusEffect.ProcEntries(new HashMap<>())).build());

    public static final Supplier<AttachmentType<PlayerCombo>> PLAYER_COMBO = ATTACHMENT_TYPES.register(
            "player_combo", () -> AttachmentType.builder(PlayerCombo::new).build());

    public static final Supplier<AttachmentType<SolidProjectile>> PROJECTILE = ATTACHMENT_TYPES.register(
            "projectile", () -> AttachmentType.builder(() -> (SolidProjectile) null)
                    .sync(new AttachmentSyncHandler<>() {
                        @Override
                        public void write(RegistryFriendlyByteBuf buf, SolidProjectile attachment, boolean initialSync) {
                            SolidProjectile.Serializer.STREAM_CODEC.encode(buf, attachment);
                        }

                        @Override
                        public @Nullable SolidProjectile read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable SolidProjectile previousValue) {
                            return SolidProjectile.Serializer.STREAM_CODEC.decode(buf);
                        }
                    })
                    .build()
    );


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
