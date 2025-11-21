package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyDamageEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.damage.DamageModifiers;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ItemModifiers;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCModEffectComponents;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.function.BiConsumer;

public class ModHelper {

    public static WorldStatus combine(WorldStatus first, WorldStatus second) {
        if (first.flag() == 0 || second.flag() == 0)
            return null;

        if (first == second)
            return null;

        int flag = first.flag() + second.flag();
        return WorldStatus.byFlag(flag);
    }

    public static void runIterationOnModHolder(ItemStack stack, IModHolder<?> holder, ModInSlotVisitor visitor) {
        ModContainer container = holder.getMods(stack);

        for (var mod : container.mods) {
            if (!mod.isEmpty()) {
                visitor.accept(mod.mod(), mod.rank(), holder);
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

    public static void runIterationOnSlots(ItemStack stack, LivingEntity entity, ModInSlotVisitor visitor) {
        runIterationOnSlots(stack, entity, null, visitor);
    }

    public static void runIterationOnSlots(ItemStack stack, LivingEntity entity, SlotGroup group, ModInSlotVisitor visitor) {
        TennoSlots slots = entity.getData(TCData.TENNO_SLOTS);
        for (var slot : slots.entrySet()) {
            if (group == null || slot.getKey() != group) {
                runIterationOnModHolder(stack, slot.getValue(), visitor);
            }
        }
    }

    public static float modifyDamage(ServerLevel level, ItemStack stack, Schema schema, LivingEntity attacker, Entity entity) {
        MutableFloat mutableFloat = new MutableFloat();
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyWeaponDamage(level, rank, schema, attacker, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    public static float modifyTypeDamage(ServerLevel level, WorldStatus status, ItemStack stack, Schema schema, LivingEntity attacker, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        modifyDamageWithAdditionalModifiers(
                level.holderOrThrow(status.getDamageType()), level, stack, schema, attacker, entity, mutableFloat
        );
        return mutableFloat.floatValue();
    }

    public static float modifyCritChance(ServerLevel level, ItemStack stack, Schema schema, LivingEntity attacker, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyCritChance(level, rank, schema, attacker, entity, mutableFloat)
        );
        modifyStatWithAdditionalModifiers(
                TCModEffectComponents.CRIT_CHANCE.get(), level, stack, schema, attacker, entity, mutableFloat
        );
        return mutableFloat.floatValue();
    }

    public static float modifyCritDamage(ServerLevel level, ItemStack stack, Schema schema, LivingEntity attacker, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyCritDamage(level, rank, schema, attacker, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    public static float modifyStatusChance(ServerLevel level, ItemStack stack, Schema schema, LivingEntity attacker, Entity entity, float damage) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        runIterationOnWeapon(
                stack, (mod, rank) -> mod.value().modifyStatusChance(level, rank, schema, attacker, entity, mutableFloat)
        );
        return mutableFloat.floatValue();
    }

    private static void modifyDamageWithAdditionalModifiers(
            Holder<DamageType> type,
            ServerLevel level,
            ItemStack stack,
            Schema schema,
            LivingEntity attacker,
            Entity entity,
            MutableFloat value
    ) {
        IWeaponModHolder<?> holder = (IWeaponModHolder<?>) stack.getItem();
        WeaponModContainer mods = (WeaponModContainer) holder.getMods(stack);
        applyDamageModifiers(
                level, schema, attacker, entity, mods.getDamageModifiers(type), value
        );
    }

    private static void modifyStatWithAdditionalModifiers(
            DataComponentType<List<ConditionalModEffect<ModValueEffect>>> type,
            ServerLevel level,
            ItemStack stack,
            Schema schema,
            LivingEntity attacker,
            Entity entity,
            MutableFloat value
    ) {
        IWeaponModHolder<?> holder = (IWeaponModHolder<?>) stack.getItem();
        WeaponModContainer mods = (WeaponModContainer) holder.getMods(stack);
        applyItemModifiers(
                level, schema, attacker, entity, mods.getItemModifiers(type), value
        );
    }

/*    public static void forEachModifier(IModHolder<?> holder, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
        runIterationOnModHolder(holder, (mod, rank) -> {
            mod.value().getEffects(TCModEffectComponents.ATTRIBUTES.get()).forEach(effect -> {
                action.accept(effect.attribute(), effect.getModifier(rank));
            });
        });
    }*/

    public static void forEachItemModifier(ItemStack stack, BiConsumer<ModifyItemEffect, Integer> modifier) {
        runIterationOnWeapon(stack, (mod, rank) -> {
            mod.value().getEffects(TCModEffectComponents.MODIFY_ITEM.get()).forEach(effect -> {
                modifier.accept(effect, rank);
            });
        });
    }

    public static void forEachItemModifier(ItemStack stack, LivingEntity entity, BiConsumer<ModifyItemEffect, Integer> modifier) {
        runIterationOnSlots(stack, entity, (mod, rank, modHolder) -> {
            mod.value().getEffects(TCModEffectComponents.MODIFY_ITEM.get()).forEach(effect -> {
                modifier.accept(effect, rank);
            });
        });
    }

    public static void forEachDamageModifier(ItemStack stack, BiConsumer<ModifyDamageEffect, Integer> modifier) {
        runIterationOnWeapon(stack, (mod, rank) -> {
            mod.value().getEffects(TCModEffectComponents.MODIFY_ITEM_DAMAGE.get()).forEach(effect -> {
                modifier.accept(effect, rank);
            });
        });
    }

    public static void forEachDamageModifier(ItemStack stack, LivingEntity entity, SlotGroup group, BiConsumer<ModifyDamageEffect, Integer> modifier) {
        runIterationOnSlots(stack, entity, group, (mod, rank, modHolder) -> {
            mod.value().getEffects(TCModEffectComponents.MODIFY_ITEM_DAMAGE.get()).forEach(effect -> {
                modifier.accept(effect, rank);
            });
        });
    }

    private static void applyItemModifiers(
            ServerLevel level,
            Schema schema,
            LivingEntity attacker,
            Entity entity,
            ItemModifiers modifiers,
            MutableFloat value
    ) {
        modifiers.forEach((location, entry) -> {
            ModifyItemEffect itemEffect = entry.effect();
            ModValueEffect valueEffect = itemEffect.value();
            int modRank = entry.modRank();
            if (itemEffect.matches(Modification.moddedAttackContext(level, schema, modRank, attacker, entity))) {
                value.setValue(valueEffect.process(modRank, entity.getRandom(), value.floatValue()));
            }
        });
    }

    private static void applyDamageModifiers(
            ServerLevel level,
            Schema schema,
            LivingEntity attacker,
            Entity entity,
            DamageModifiers modifiers,
            MutableFloat value
    ) {
        modifiers.forEach((location, entry) -> {
            ModifyDamageEffect damageEffect = entry.effect();
            ModValueEffect valueEffect = damageEffect.value();
            int modRank = entry.modRank();
            if (damageEffect.matches(Modification.moddedAttackContext(level, schema, modRank, attacker, entity))) {
                value.setValue(valueEffect.process(modRank, entity.getRandom(), value.floatValue()));
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
