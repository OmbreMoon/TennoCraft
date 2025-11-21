package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCMeleeWeaponMods;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MeleeWeaponItem extends AbstractWeaponItem<MeleeWeaponSchema> {

    public MeleeWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.MELEE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            MeleeWeaponHandler handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
            if (handler != null) {
                ModContainer mods = this.getMods(stack);
                Holder<Modification> mod = level.registryAccess().holderOrThrow(TCMeleeWeaponMods.FURY);
                mods.modCache.setMod(0, new ModInstance(mod, 3));
                this.confirmModChanges(level, stack);
                log(mods);
                log(handler.getSchema().getModdedCritChance((ServerLevel) level, stack, player, null));
                log(handler.getTag());
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
//        WeaponDamageResult result = WeaponDamageResult.calculateMeleeResult(this, stack, target);
        MeleeWeaponHandler handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            ModContainer mods = this.getMods(stack);
            log(mods);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public ModContainer getMods(ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            return handler.getMods();
        }
        return null;
    }

    @Override
    public AttributeMap getStats(ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            return handler.getStats();
        }
        return null;
    }

    @Override
    public void confirmModChanges(Level level, ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess();
            handler.confirmModChanges(level, stack);
        }
    }

    public static ItemStack createWithMeleeSchema(SchemaHolder<MeleeWeaponSchema> schema, HolderLookup.Provider registries) {
        ItemStack weapon = new ItemStack(TCItems.MELEE_WEAPON.get());
        weapon.set(TCData.SCHEMA, schema);
        weapon.set(TCData.MELEE_WEAPON_HANDLER, new MeleeWeaponHandler(new CompoundTag(), schema.schema(), registries));
        return weapon;
    }
}
