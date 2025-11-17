package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCMeleeWeaponMods;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MeleeWeaponItem extends AbstractWeaponItem<MeleeWeaponHandler> {

    public MeleeWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.MELEE;
    }

    @Override
    public ModContainer getMods(ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            return handler.getMods();
        }
        return null;
    }

    @Override
    public AttributeMap getStats(ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            return handler.getStats();
        }
        return null;
    }

    @Override
    public void confirmModChanges(ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(stack);
            handler.confirmModChanges(stack);
        }
    }

    public static ItemStack createWithMeleeSchema(SchemaHolder schema, HolderLookup.Provider registries) {
        if (!(schema.schema() instanceof MeleeWeaponSchema weaponSchema)) {
            throw new IllegalStateException("Tried to create melee weapon with invalid schema: " + schema.schemaKey());
        }

        ItemStack weapon = new ItemStack(TCItems.MELEE_WEAPON.get());
        weapon.set(TCData.SCHEMA, schema);
        weapon.set(TCData.MELEE_WEAPON_HANDLER, new MeleeWeaponHandler(new CompoundTag(), weaponSchema, registries));
        return weapon;
    }
}
