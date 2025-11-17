package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
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

public abstract class RangedWeaponItem extends AbstractWeaponItem<RangedWeaponSchema> {

    public RangedWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public ModContainer getMods(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            return handler.getMods();
        }
        return null;
    }

    @Override
    public AttributeMap getStats(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            return handler.getStats();
        }
        return null;
    }

    @Override
    public void confirmModChanges(ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            handler.confirmModChanges(stack);
        }
    }

    public static ItemStack createWithRangedSchema(SchemaHolder schema, HolderLookup.Provider registries) {
        if (!(schema.schema() instanceof RangedWeaponSchema weaponSchema)) {
            throw new IllegalStateException("Tried to create ranged weapon with invalid schema: " + schema.schemaKey());
        }

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
