package com.ombremoon.tennocraft.object.item.weapon;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.network.weapon.WeaponType;
import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;
import com.ombremoon.tennocraft.player.FrameAttribute;
import com.ombremoon.tennocraft.util.WeaponUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWeaponItem extends Item implements IModHolder {
    private final WeaponType weaponType;
    private final float critChance;
    private final float critMultiplier;
    private final float impactDamage;
    private final float punctureDamage;
    private final float slashDamage;
    private final float statusChance;
    private final float heatDamage;
    private final Map<DamageType, Float> damageMap;

    public AbstractWeaponItem(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties.stacksTo(1));
        this.weaponType = weaponProperties.weaponType;
        this.critChance = weaponProperties.critChance;
        this.critMultiplier = weaponProperties.critMultiplier;
        this.impactDamage = weaponProperties.impactDamage;
        this.punctureDamage = weaponProperties.punctureDamage;
        this.slashDamage = weaponProperties.slashDamage;
        this.heatDamage = weaponProperties.heatDamage;
        this.statusChance = weaponProperties.statusChance;
        this.damageMap = weaponProperties.damageMap;
    }

    public Map<DamageType, Float> getDamageMap() {
        return this.damageMap;
    }

    public float getModdedDamage(ItemStack itemStack) {
        return (this.getTotalBaseDamage() * (1 + WeaponUtil.getDamageModifier(itemStack)));
    }

    public float getModdedCritChance(ItemStack itemStack) {
        return (this.getCritChance() * (1 + WeaponUtil.getCritChanceModifier(itemStack)));
    }

    public float calculateCritDamage(ItemStack itemStack) {
        return this.getCritMultiplier() * (1 + WeaponUtil.getCritDamageModifier(itemStack));
    }

    public float getModdedStatus(ItemStack itemStack) {
        return this.getStatusChance() * (1 + WeaponUtil.getStatusModifier(itemStack));
    }

    public boolean isCriticalHit(float critChance) {
        return RandomUtil.percentChance(critChance);
    }

    public float getModdedAttributeDamage(FrameAttribute frameAttribute, ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(frameAttribute, itemStack);
    }

    public ResourceLocation getWeaponLocation() {
        return ForgeRegistries.ITEMS.getKey(this);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public WeaponType getWeaponType() {
        return this.weaponType;
    }

    public float getCritChance() {
        return this.critChance;
    }

    public float getCritMultiplier() {
        return this.critMultiplier;
    }

    public float getImpactDamage() {
        return this.impactDamage;
    }

    public float getPunctureDamage() {
        return this.punctureDamage;
    }

    public float getSlashDamage() {
        return this.slashDamage;
    }

    public float getHeatDamage() {
        return this.heatDamage;
    }

    public float getPhysicalDamage() {
        return this.getImpactDamage() + this.getPunctureDamage() + this.getSlashDamage();
    }

    public float getElementalDamage() {
        return this.getHeatDamage();
    }

    public float getTotalBaseDamage() {
        return this.getPhysicalDamage() + this.getElementalDamage();
    }

    public float getStatusChance() {
        return this.statusChance;
    }
}
