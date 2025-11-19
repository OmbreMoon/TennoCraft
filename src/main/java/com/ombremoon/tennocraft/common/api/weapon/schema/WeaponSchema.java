package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class WeaponSchema implements Schema {
    protected final GeneralSchema general;

    WeaponSchema(GeneralSchema general) {
        this.general = general;
    }

    public GeneralSchema getGeneral() {
        return this.general;
    }

    public abstract int getBaseDamage(@Nullable TriggerType triggerType);

    public abstract WeaponDamageResult.Distribution getBaseDamageDistribution(@Nullable TriggerType triggerType);

    public abstract float getModdedCritChance(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType);

    public abstract float getModdedCritDamage(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType);

    public abstract float getModdedStatusChance(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType);

    public abstract float getModdedRivenDisposition(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType);
}
