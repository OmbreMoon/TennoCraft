package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItem extends Item {
    public ModItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Holder<Modification> mod = stack.get(TCData.MOD);
        return mod != null ? mod.value().name() : super.getName(stack);
    }

    public static ItemStack createForMod(ModInstance instance) {
        ItemStack stack = new ItemStack(TCItems.MOD.get());
        stack.set(TCData.MOD, instance.mod());
        return stack;
    }
}
