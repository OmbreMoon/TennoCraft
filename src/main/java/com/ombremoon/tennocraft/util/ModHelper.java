package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.*;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.ombremoon.tennocraft.common.api.mod.Modification.entityContext;

public class ModHelper {

    public static WorldStatus combine(WorldStatus first, WorldStatus second) {
        if (first.flag() == 0 || second.flag() == 0)
            return null;

        if (first == second)
            return null;

        int flag = first.flag() + second.flag();
        return WorldStatus.byFlag(flag);
    }

    public static void runIterationOnModHolder(IModHolder<?> holder, ModVisitor visitor) {
        ModContainer container = holder.getMods();

        for (var mod : container.mods) {
            if (!mod.isEmpty()) {
                visitor.accept(mod.mod(), mod.rank());
            }
        }
    }

    public static void runIterationOnWeapon(ItemStack stack, ModVisitor visitor) {
        if (stack.getItem() instanceof IModHolder<?> holder) {
            ModContainer container = holder.getMods(stack);

            for (var mod : container.mods) {
                if (!mod.isEmpty()) {
                    visitor.accept(mod.mod(), mod.rank());
                }
            }
        }
    }

    public static float modifyDamage(ServerLevel level, ItemStack stack, Schema schema, Entity entity) {
        MutableFloat mutableFloat = new MutableFloat();
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyWeaponDamage(level, rank, schema, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    public static float modifyTypeDamage(ServerLevel level, WorldStatus status, ItemStack stack, Schema schema, Entity entity) {
        MutableFloat mutableFloat = new MutableFloat();
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyTypeDamage(level, status, rank, schema, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    public static float modifyCritChance(ServerLevel level, ItemStack stack, Schema schema, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyCritChance(level, rank, schema, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    public static float modifyCritDamage(ServerLevel level, ItemStack stack, Schema schema, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyCritDamage(level, rank, schema, entity, mutableFloat)
        );
       modifyStatWithAdditionalModifiers(
               TCModEffectComponents.CRIT_CHANCE.get(), level, stack, schema, entity, mutableFloat
       );
        return mutableFloat.floatValue();
    }

    public static float modifyStatusChance(ServerLevel level, ItemStack stack, Schema schema, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyStatusChance(level, rank, schema, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    private static void modifyStatWithAdditionalModifiers(
            DataComponentType<List<ConditionalModEffect<ModValueEffect>>> type,
            ServerLevel level,
            ItemStack stack,
            Schema schema,
            Entity entity,
            MutableFloat value
    ) {
        IWeaponModHolder<?> holder = (IWeaponModHolder<?>) stack.getItem();
        WeaponModContainer mods = (WeaponModContainer) holder.getMods(stack);
        applyItemModifiers(
                level,
                schema,
                entity,
                mods.getItemModifiers(type),
                value
        );
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
        });
    }

    private static void applyItemModifiers(
            ServerLevel level,
            Schema schema,
            Entity entity,
            WeaponModContainer.ItemModifiers modifiers,
            MutableFloat value
    ) {
        modifiers.forEach((location, effect) -> {
            var valueEffect = effect.left();
            int modRank = effect.right();
            if (valueEffect.matches(Modification.entityContext(level, schema, modRank, entity, entity.position()))) {
                value.setValue(valueEffect.effect().process(modRank, entity.getRandom(), value.floatValue()));
            }
        });
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
