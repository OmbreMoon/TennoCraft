package com.ombremoon.tennocraft.common.modholder;

import com.ombremoon.tennocraft.common.modholder.api.MineFrame;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.world.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import com.ombremoon.tennocraft.util.Loggable;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;

import java.util.Map;

public class FrameHandler implements INBTSerializable<CompoundTag>, Loggable {
    private static final Logger LOGGER = Constants.LOG;
    private final Map<ResourceKey<Schema>, MineFrame> frames = new Reference2ObjectArrayMap<>();
    public LivingEntity owner;
    private Level level;
    private ResourceKey<Schema> currentFrame;
    private ItemStack transferenceKey;
    private boolean isTransfered;
    private boolean initialized;

    /**
     * Syncs spells handler data from the server to the client.
     */
//    public void sync() {
//        if (this.caster instanceof Player player && !this.isClientSide())
//            PayloadHandler.syncHandlerToClient(player);
//    }

    /**
     * <p>
     * Initializes the frame handler.
     * </p>
     * See:
     * <ul>
     *     <li>{@code SpellEventListener}</li>
     *     <li>{@code SkillHolder}</li>
     *     <li>{@code UpgradeTree}</li>
     * </ul>
     * @param caster
     */
    public void initData(LivingEntity caster) {
        this.owner = caster;
        this.level = caster.level();
        this.initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public ResourceKey<Schema> getFrameKey() {
        return this.currentFrame;
    }

    public void selectFrame(ResourceKey<Schema> frame) {
        this.currentFrame = frame;
    }

    public ItemStack getOrCreateKey() {
        if (this.transferenceKey == null) {
            this.transferenceKey = TransferenceKeyItem.createWithFrame(this.level, this.currentFrame);
        }

        return this.transferenceKey;
    }

    public boolean isTransfered() {
        return this.isTransfered;
    }

    public void toggleTransference() {
        this.isTransfered = !this.isTransfered;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("Transference", this.isTransfered);
        ResourceKey.codec(Keys.SCHEMA)
                .encodeStart(NbtOps.INSTANCE, this.currentFrame)
                .resultOrPartial(LOGGER::error)
                .ifPresent(tag -> nbt.put("CurrentFrame", tag));
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.isTransfered = tag.getBoolean("Transference");
        ResourceKey.codec(Keys.SCHEMA)
                .parse(NbtOps.INSTANCE, tag.get("CurrentFrame"))
                .resultOrPartial(LOGGER::error)
                .ifPresent(this::selectFrame);
    }
}
