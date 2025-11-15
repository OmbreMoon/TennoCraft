package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.util.Loggable;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModItem extends Item implements Loggable {
    public ModItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        ModInstance instance = stack.get(TCData.MOD);
        return instance != null ? instance.mod().value().name() : super.getName(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            log(stack.get(TCData.MOD).save());
        }
        return super.use(level, player, usedHand);
    }

    public static ItemStack createForMod(ModInstance instance) {
        ItemStack stack = new ItemStack(TCItems.MOD.get());
        stack.set(TCData.MOD, instance);
        return stack;
    }
}
