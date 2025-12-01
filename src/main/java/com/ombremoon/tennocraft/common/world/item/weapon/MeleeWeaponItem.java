package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboType;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.mods.TCMeleeWeaponMods;
import com.ombremoon.tennocraft.common.init.mods.TCPrimaryWeaponMods;
import com.ombremoon.tennocraft.common.init.mods.TCSecondaryWeaponMods;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.util.StatusHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

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
                ModContainer mods = this.getMods(player, stack);
                Holder<Modification> mod = level.registryAccess().holderOrThrow(TCPrimaryWeaponMods.CRYO_ROUNDS);
//                mods.loadCache();
//                mods.modCache.setMod(2, new ModInstance(mod, 3));
//                this.confirmModChanges(player, stack);
                log(handler.test);
                log(handler.getTag());
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            WeaponDamageResult result = WeaponDamageResult.calculateMelee(stack, attacker, target);
            log(result);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public ModContainer getMods(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            return handler.getMods();
        }
        return null;
    }

    @Override
    public void confirmModChanges(Player player, ItemStack stack) {
        var handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(player.registryAccess());
            handler.confirmModChanges(player, stack);
        }
    }

    public static ItemStack createWithMeleeSchema(SchemaHolder<MeleeWeaponSchema> schema, HolderLookup.Provider registries) {
        ItemStack weapon = new ItemStack(TCItems.MELEE_WEAPON.get());
        weapon.set(TCData.SCHEMA, schema);
        weapon.set(TCData.MELEE_WEAPON_HANDLER, new MeleeWeaponHandler(new CompoundTag(), schema.schema(), registries));
        return weapon;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
