package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.weapon.WeaponProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class SecondaryWeapon extends AbstractProjectileWeapon {

    public SecondaryWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
    }

    @Override
    public ModType getModType() {
        return ModType.SECONDARY;
    }
}
