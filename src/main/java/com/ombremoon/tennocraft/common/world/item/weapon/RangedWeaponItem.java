package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
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

import java.util.List;

public abstract class RangedWeaponItem extends AbstractWeaponItem<RangedWeaponSchema> implements IRangedModHolder {

    public RangedWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
            if (handler != null) {
                ModContainer mods = this.getMods(stack);
//                Holder<Modification> mod = level.registryAccess().holderOrThrow(TCSecondaryWeaponMods.PISTOL_GAMBIT);
//                mods.modCache.setMod(0, new ModInstance(mod, 3));
//                this.confirmModChanges(stack);
                log(mods);
//                log(this.getMods(stack));
                log(handler.getTag());
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
    public AttributeMap getStats(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            return handler.getStats();
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
    public void confirmModChanges(Level level, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            handler.confirmModChanges(level, stack);
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
}
