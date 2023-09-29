package com.ombremoon.tennocraft.player.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class AbstractFrameAbility {

    public final AbilityType<?> abilityType;
    public final int energyRequired;
    public final int energyPerSecond;

    public ServerLevel level;
    public BlockPos blockPos;
    public UUID userID;
    public UUID targetID;
    public Entity targetEntity;

    protected long ticks = 0;
    public boolean isNotActive = false;
    public boolean isStarting = false;

    public AbstractFrameAbility(AbilityType<?> abilityType, int energyRequired, int energyPerSecond) {
        this.abilityType = abilityType;
        this.energyRequired = energyRequired;
        this.energyPerSecond = energyPerSecond;
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("registryName", getAbilityType().getRegistryName().toString());
        nbt.putString("dimension", level.dimension().location().toString());
        nbt.putInt("x", blockPos.getX());
        nbt.putInt("y", blockPos.getY());
        nbt.putInt("z", blockPos.getZ());
        nbt.putUUID("user", userID);
        if (targetID != null) nbt.putUUID("target", targetID);
        nbt.putLong("ticks", ticks);
        nbt.putBoolean("isActive", isNotActive);

        saveAdditionalData(nbt);
        return nbt;
    }

    public boolean load(CompoundTag nbt, Level level) {
        setLevel(level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("dimension")))));
        setBlockPos(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        userID = nbt.getUUID("user");
        if (nbt.contains("target")) targetID = nbt.getUUID("target");
        ticks = nbt.getLong("ticks");
        isNotActive = nbt.getBoolean("isActive");

        return loadAdditionalData(nbt, level);
    }

    /*
    * Override to save any additional data to the frame abilities persistent data
    * @param nbt
    * @return final nbt
    */
    protected void saveAdditionalData(CompoundTag nbt) {

    }

    /*
    * Override to load any additional data, return if needed for whatever reason
    * @param nbt, level
    */
    protected boolean loadAdditionalData(CompoundTag nbt, Level level) {
        return true;
    }

    /*
    * Override to add additional effects at the start of ability cast
    */
    protected void onStart() {

    }

    /*
     * Override to add effects that run every tick
     */
    protected void onTick() {

    }

    /*
     * Override to add additional effects at the end of ability duration
     */
    protected void onStop() {

    }

    public void tick() {
        if (!level.isClientSide()) {
            ticks++;
            if (isStarting) {
                System.out.println("Test");
                if (ticks % 20 == 0) {
                    castAbility();
                }
            } else if (!isNotActive) {
                System.out.println(ticks);
                onTick();
            }
        }
    }

    protected void castAbility() {
        this.isStarting = false;
        onStart();
    }

    public void endAbility() {
        onStop();
        this.isStarting = false;
        this.isNotActive = true;
        this.ticks = 0;
    }

    protected Entity getTargetEntity() {
        Entity targetEntity = level.getServer().getPlayerList().getPlayer(targetID);

        if (targetEntity != null) {
            return targetEntity;
        } else {
            for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
                targetEntity = serverLevel.getEntity(targetID);
                if (targetEntity != null) return targetEntity;
            }
        }
        return null;
    }

    public AbilityType<?> getAbilityType() {
        return this.abilityType;
    }

    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setUser(Player player) {
        this.userID = player.getUUID();
    }

    public void start() {
        this.isStarting = true;
    }

    public boolean isStarting() {
        return  this.isStarting;
    }
}
