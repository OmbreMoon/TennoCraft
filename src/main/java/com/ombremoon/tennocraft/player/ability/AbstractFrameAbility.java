package com.ombremoon.tennocraft.player.ability;

import com.google.common.collect.Multimap;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.entity.projectile.FrameProjectile;
import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractFrameAbility {
    private String descriptionId;
    private float abilityDuration;
    private float moddedDuration = 0;

    private final AbilityType<?> abilityType;
    private final int energyRequired;
    private final int energyPerSecond;
    private final int durationInTicks;
    private final float initialRange;

    public ServerLevel level;
    public BlockPos blockPos;
    public UUID userID;
    public UUID targetID;
    public Entity targetEntity;
    protected long ticks = 0;
    public boolean isNotActive = false;
    public boolean isStarting = false;

    public AbstractFrameAbility(AbilityType<?> abilityType, int energyRequired, int energyPerSecond, int durationInTicks, float initialRange) {
        this.abilityType = abilityType;
        this.energyRequired = energyRequired;
        this.energyPerSecond = energyPerSecond;
        this.durationInTicks = durationInTicks;
        this.initialRange = initialRange;
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

    public int getEnergyRequired() {
        return this.energyRequired;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("ability", FrameAbilities.FRAME_ABILITY.getRegistryName());
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getDescriptionId());
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
                if (ticks % 10 == 0) {
                    castAbility();
                }
            } else if (!isNotActive) {
                onTick();
                if (ticks % getAbilityDuration() == 0
                        || (level.getPlayerByUUID(userID) != null && !FrameUtil.hasOnFrame(Objects.requireNonNull(level.getPlayerByUUID(userID))))
                        || (level.getPlayerByUUID(userID) != null && !level.getPlayerByUUID(userID).isAlive())
                        || (level.getPlayerByUUID(userID) == null)) {
                    System.out.println(ticks);
                    endAbility();
                }
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

    protected void addModifier(LivingEntity livingEntity, Attribute attribute, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
        AttributeInstance attributeInstance = getAttributeInstance(livingEntity, attribute);
        AttributeModifier attributeModifier = new AttributeModifier(uuid, name, amount, operation);
        if (!attributeInstance.hasModifier(attributeModifier)) {
            attributeInstance.addPermanentModifier(attributeModifier);
        }
    }

    protected void removeModifier(LivingEntity livingEntity, Attribute attribute, UUID uuid) {
        AttributeInstance attributeInstance = getAttributeInstance(livingEntity, attribute);
        attributeInstance.removePermanentModifier(uuid);
    }

    public AbilityType<?> getAbilityType() {
        return this.abilityType;
    }

    public UUID getUserID() {
        return this.userID;
    }

    public void setUser(Player player) {
        this.userID = player.getUUID();
    }

    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void start() {
        initModifiers();

        this.isStarting = true;
    }

    public boolean isStarting() {
        return this.isStarting;
    }

    private AttributeInstance getAttributeInstance(LivingEntity livingEntity, Attribute attribute) {
        AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(attribute);
        return attributeInstance;
    }

    private void setAbilityDuration(int durationInTicks) {
        if (level.getPlayerByUUID(userID) != null) {
            Player player = level.getPlayerByUUID(userID);
            ItemStack frameStack = FrameUtil.getFrameStack(player);
            this.abilityDuration = Math.round((durationInTicks * (1 + getModdedDuration(frameStack))));
        }
    }

    public float getAbilityDuration() {
        return this.abilityDuration;
    }

    private void initModifiers() {
        setAbilityDuration(this.durationInTicks);
    }

    //Attributes

    //FIX!!!!!!!
    public float getModdedDuration(ItemStack itemStack) {
        Map<FrameAttribute, Float> frameAttributes = AttributeHandler.getFrameAttributes(itemStack);
        for (Map.Entry<FrameAttribute, Float> entry : frameAttributes.entrySet()) {
            FrameAttribute frameAttribute = entry.getKey();
            float attributeModifier = entry.getValue();
            if (frameAttribute == FrameAttributes.DURATION.get()) {
                moddedDuration += attributeModifier;
                System.out.println(moddedDuration);
            }
        }
        return moddedDuration;
    }
}
