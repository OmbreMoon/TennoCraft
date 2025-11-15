package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.RangedWeaponSchema;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class RangedWeapon extends AbstractWeapon<RangedWeaponSchema> {

    public RangedWeapon(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            Holder<Modification> mod = level.registryAccess().holderOrThrow(TCSecondaryWeaponMods.PISTOL_GAMBIT);
            var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
            if (handler != null) {
                handler.cycleAlternateFire(stack);
                ModContainer mods = this.getMods(stack);
                log(mods);
//                mods.modCache.setMod(0, new ModInstance(mod, 3));
//                handler.confirmModChanges(stack);
//                mods.confirmMods(this, stack);
                log(handler.getTriggerType());
                log(this.getStats(stack).getInstance(TCAttributes.CRIT_CHANCE).getValue());
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
