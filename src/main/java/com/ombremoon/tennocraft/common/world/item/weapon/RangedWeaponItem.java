package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackProperty;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

public abstract class RangedWeaponItem extends AbstractWeaponItem<RangedWeaponSchema> implements IRangedModHolder {

    public RangedWeaponItem(Properties properties) {
        super(properties);
    }

    //MAKE PROJECTILE ENTITY PREDICATE
    //MAKE TRIGGER TYPE (Ex: Trigger -> Charge, Charge -> level < minLevel) PREDICATE

    /**
     * Projectile Interactions...
     *
     * Projectile Speed
     * Projectile Size - DONE
     * Optional Lifetime - DONE
     * - If <= 0, lasts until gun reload
     * Optional Range - DONE
     * Optional Max Live Projectiles
     */

    /**
     * Bullet Interactions...
     *
     * Damage Entity
     * - Charged Based Damage
     * Consume Ammo
     * Life Steal
     * Add Status Effect
     * Summon Status Effect Cloud
     * Bullet Attractor Effect
     * - Origin
     * - Range
     * - Duration
     * Stick to Surface
     * - Pull in enemies
     * - DoT AoE
     * Impale Enemy
     * Modify Spread
     * - Charge Based Spread
     * Explode (On Impact)
     * Explode (Mid-Flight)
     * - Explode when...
     *  - At Tick
     *  - Manual
     * Summon Entity
     * Summon Projectile
     * Modify Attack Schema
     * Modify Projectile Size
     * Modify Projectile Lifetime
     * Strengthen Colliding Projectiles
     * Bounce
     * - Max Bounces
     * Merge
     *
     * Projectile Events
     * - On Fire
     * - On Alternate Fire
     * - On Hit Entity
     * - On Hit Block
     * - Tick
     */

    /**
     * Gun Interactions...
     *
     * Ammo Cost
     * - If <= 0, consume entire clip
     * - Consume when...
     *  - Fire Gun
     *  - Projectile Deal Damage
     *
     * Reload Type (Serializer)
     * - Ammo
     * - Over Time
     * - Resource (Health/Shield/Energy)
     *
     * Gun Animations
     * - Idle
     * - Fire
     */


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
            if (handler != null) {
                log(this);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    @Override
    public ModContainer getMods(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            return handler.getMods();
        }
        return null;
    }

    @Override
    public TriggerType<?> getTriggerType(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            return handler.getTriggerType();
        }
        return null;
    }

    @Override
    public void confirmModChanges(Player player, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(player.registryAccess());
            handler.confirmModChanges(player, stack);
        }
    }

    public static ItemStack createWithRangedSchema(SchemaHolder<RangedWeaponSchema> schema, HolderLookup.Provider registries) {
        RangedWeaponSchema weaponSchema = schema.schema();
        Modification.Compatibility type = weaponSchema.getCompatibility();
        Item item;
        switch (type) {
            case SECONDARY -> item = TCItems.SECONDARY_WEAPON.get();
            default -> item = TCItems.PRIMARY_WEAPON.get();
        }

        ItemStack weapon = new ItemStack(item);
        weapon.set(TCData.SCHEMA, schema);
        weapon.set(TCData.RANGED_WEAPON_HANDLER, new RangedWeaponHandler(new CompoundTag(), weaponSchema, registries));
        return weapon;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Idle
        //Fire
    }
}
