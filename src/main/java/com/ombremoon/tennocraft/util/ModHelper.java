package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.world.item.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class ModHelper {

    public static void runIterationOnModHolder(IModHolder<?> holder, ModVisitor visitor) {
        ModContainer container = holder.getMods();

        for (var mod : container.mods) {
            visitor.accept(mod.mod(), mod.rank());
        }
    }

    public static void runIterationOnWeapon(ItemStack stack, ModVisitor visitor) {
        if (stack.getItem() instanceof IModHolder<?> holder) {
            ModContainer container = holder.getMods(stack);

            for (var mod : container.mods) {
                visitor.accept(mod.mod(), mod.rank());
            }
        }
    }

    public static void forEachModifier(IModHolder<?> holder, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
        runIterationOnModHolder(holder, (mod, rank) -> {
            mod.value().getEffects(TCModEffectComponents.ATTRIBUTES.get()).forEach(effect -> {
                action.accept(effect.attribute(), effect.getModifier(rank));
            });
        });
    }

    public static void forEachModifier(ItemStack stack, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
        runIterationOnWeapon(stack,(mod, rank) -> {
            mod.value().getEffects(TCModEffectComponents.ATTRIBUTES.get()).forEach(effect -> {
                action.accept(effect.attribute(), effect.getModifier(rank));
            });
        } );
    }

    @FunctionalInterface
    public interface ModInSlotVisitor {
        void accept(Holder<Modification> mod, int rank, IModHolder<?> modHolder);
    }

    @FunctionalInterface
    public interface ModVisitor {
        void accept(Holder<Modification> mod, int rank);
    }
}
