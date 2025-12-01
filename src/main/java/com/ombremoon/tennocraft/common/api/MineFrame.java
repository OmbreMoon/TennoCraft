package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.mod.EntityModContainer;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.api.handler.FrameHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public class MineFrame implements IEntityModHolder<FrameSchema>, INBTSerializable<CompoundTag> {
    private final SchemaHolder<FrameSchema> schemaHolder;
    private final FrameSchema schema;
    private final FrameHandler handler;
    private final List<AbilityType<?>> abilities = new ObjectArrayList<>();
    private final AbilityType<?> passive;
    private final Component description;
    private final AttributeMap stats;
    private final EntityModContainer mods;
    public float health;
    private float shield;
    private float energy;

    public MineFrame(SchemaHolder<FrameSchema> schemaHolder, FrameHandler handler) {
        this.schemaHolder = schemaHolder;
        this.handler = handler;
        this.schema = schemaHolder.schema();
        this.abilities.addAll(schema.abilities());
        this.passive = schema.passive().orElse(null);
        this.description = schema.description();
        this.mods = new EntityModContainer(Modification.Compatibility.FRAME, schema);
        this.stats = new AttributeMap(createFrameAttributes(schema));
    }

    private static AttributeSupplier createFrameAttributes(FrameSchema schema) {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, schema.health().leftInt())
                .add(TCAttributes.MAX_SHIELD, schema.shield().leftInt())
                .add(TCAttributes.MAX_ENERGY, schema.energy().leftInt())
                .add(TCAttributes.SPRINT_SPEED, schema.sprintSpeed())
                .add(TCAttributes.ABILITY_DURATION)
                .add(TCAttributes.ABILITY_EFFICIENCY)
                .add(TCAttributes.ABILITY_RANGE)
                .add(TCAttributes.ABILITY_STRENGTH)
                .build();
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean hurt(DamageSource source, float amount) {
        boolean flag2 = amount > 0;
        this.setHealth(Mth.clamp(this.health - amount, 0.0F, this.getMaxHealth()));
        return flag2;
    }

    public float getMaxHealth() {
        return (float) this.getStats().getValue(Attributes.MAX_HEALTH);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.FRAME;
    }

    @Override
    public SchemaHolder<FrameSchema> schemaHolder(ItemStack itemStack) {
        return this.schemaHolder;
    }

    @Override
    public FrameSchema schema(ItemStack stack) {
        return this.schema;
    }

    @Override
    public ModContainer getMods(LivingEntity entity, ItemStack stack) {
        return this.mods;
    }

    @Override
    public void confirmModChanges(Player player, ItemStack stack) {
        this.mods.confirmMods(player, this, stack);
    }

    @Override
    public AttributeMap getStats() {
        return this.stats;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        ListTag statList = this.stats.save();
        nbt.put("Mods", this.mods.serializeNBT(provider));
        nbt.put("Stats", statList);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.mods.deserializeNBT(provider, tag);

        if (tag.contains("Stats")) {
            ListTag listTag = tag.getList("Stats", 10);
            this.stats.load(listTag);
        }
    }
}
