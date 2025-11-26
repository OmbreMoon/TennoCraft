package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

public abstract class RangedWeaponItem extends AbstractWeaponItem<RangedWeaponSchema> implements IRangedModHolder {

    public RangedWeaponItem(Properties properties) {
        super(properties);
    }

    //MAKE PROJECTILE ENTITY PREDICATE

    /**
     * Projectile Interactions...
     *
     * Projectile Speed
     * Projectile Size - DONE
     * Optional Lifetime - DONE
     * - If <= 0, lasts until gun reload
     * Optional Range
     * Optional Max Live Projectiles
     */

    /**
     * Bullet Interactions...
     *
     * Damage Entity
     * - Charged Based Damage
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
     * Reload Type (Serializer) - DONE
     * - Ammo - DONE
     * - Over Time - DONE
     * - Resource (Health/Shield/Energy) - DONE
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
                RangedWeaponSchema schema = this.schema(stack);
                TriggerType triggerType = this.getTriggerType(stack);
                RangedAttackSchema attacks = schema.getProperties().getAttack(triggerType);
                RangedAttack attack = attacks.getAttack();
                BulletProjectile projectile = new BulletProjectile(level, player, attack, (SolidProjectile) attack.projectileType(), stack, this);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                level.addFreshEntity(projectile);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public ModContainer getMods(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            return handler.getMods();
        }
        return null;
    }

    @Override
    public TriggerType getTriggerType(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            return handler.getTriggerType();
        }
        return null;
    }

    @Override
    public void confirmModChanges(Player player, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            handler.confirmModChanges(player, stack);
        }
    }

    public static ItemStack createWithRangedSchema(SchemaHolder<RangedWeaponSchema> schema, HolderLookup.Provider registries) {
        RangedWeaponSchema weaponSchema = schema.schema();
        Modification.Compatibility type = weaponSchema.getGeneral().layout().compatibility();
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
