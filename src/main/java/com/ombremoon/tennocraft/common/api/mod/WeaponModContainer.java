package com.ombremoon.tennocraft.common.api.mod;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.mod.effects.damage.DamageModifiers;
import com.ombremoon.tennocraft.common.api.mod.effects.item.ItemModifiers;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.StatusHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DON'T FORGET AURA AND DURATION USES CASES FOR MODIFIERS
public class WeaponModContainer extends ModContainer {
    private final WeaponSchema schema;
    public final List<DamageValue> weaponDamageTypes = new ObjectArrayList<>();
    public final Map<Holder<DamageType>, DamageModifiers> damageModifiers = new Object2ObjectOpenHashMap<>();
    public final Map<DataComponentType<?>, ItemModifiers> itemModifiers = new Object2ObjectOpenHashMap<>();

    public WeaponModContainer(Modification.Compatibility type, WeaponSchema schema) {
        this(type, schema, false);
    }

    WeaponModContainer(Modification.Compatibility type, WeaponSchema schema, boolean cache) {
        super(type, schema, cache);
        this.schema = schema;
    }

    @Override
    protected ModContainer createCache() {
        return new WeaponModContainer(this.type, this.schema, true);
    }

    @Override
    public void confirmMods(Player player, IModHolder<?> holder, ItemStack stack) {
        super.confirmMods(player, holder, stack);
        this.collectModifiers(player, stack);
    }

    public void collectModifiers(Player player, ItemStack stack) {
        this.weaponDamageTypes.clear();
        this.itemModifiers.forEach((dataComponentType, modifiers) -> modifiers.removeIf((location, entry) -> !entry.shouldPersist()));
        this.damageModifiers.forEach((dataComponentType, modifiers) -> modifiers.removeIf((location, entry) -> !entry.shouldPersist()));
        this.collectItemModifiersFromComponent(player, stack);
        this.collectDamageModifiers(player, stack);
    }

    public void collectItemModifiersFromComponent(Player player, ItemStack stack) {
        ModHelper.forEachItemModifier(stack, player, (effect, rank) -> {
            var componentType = effect.withComponent();
            ItemModifiers modifiers = this.getItemModifiers(componentType);
            modifiers.put(effect.id(), rank, effect, false);
        });
    }

    //WRITE A SCHEMA PREDICATE

    public void collectDamageModifiers(Player player, ItemStack stack) {
        List<DamageValue> orderedValues = new ArrayList<>();
        IWeaponModHolder<?> modHolder = (IWeaponModHolder<?>) stack.getItem();
        ModHelper.forEachDamageModifier(stack, (effect, modRank) -> {
            Holder<DamageType> damageType = player.registryAccess().holderOrThrow(effect.damageType());
            if (effect.conditions().isPresent()) {
                DamageModifiers modifiers = this.getDamageModifiers(damageType);
                modifiers.put(effect.id(), modRank, effect, false);
            } else {
                float amount = effect.value().process(modRank, player.getRandom(), 0.0F);
                DamageValue value = new DamageValue(damageType, amount);
                addOrMergeType(orderedValues, value);
            }
        });

        TriggerType triggerType = modHolder instanceof IRangedModHolder rangedModHolder ? rangedModHolder.getTriggerType(stack) : null;
        var distribution = this.schema.getBaseDamageDistribution(triggerType);
        distribution.ifElementalPresent((damageType, amount) -> addOrMergeType(orderedValues, new DamageValue(damageType, amount)));

        this.weaponDamageTypes.addAll(reorderDamageTypes(player.registryAccess(), orderedValues));

        //Loop through external mod containers (sans current group) and add to map
    }

    public static List<DamageValue> reorderDamageTypes(RegistryAccess registryAccess, List<DamageValue> orderedValues) {
        int i = 0;
        List<DamageValue> damageValues = new ArrayList<>();
        while (i < orderedValues.size()) {
            DamageValue current = orderedValues.get(i);

            if (i == orderedValues.size() - 1) {
                damageValues.add(current);
                break;
            }

            DamageValue next = orderedValues.get(i + 1);
            WorldStatus currentStatus = StatusEffect.getStatusFromType(current.damageType().getKey());
            WorldStatus nextStatus = StatusEffect.getStatusFromType(next.damageType().getKey());

            WorldStatus combinedStatus = ModHelper.combine(currentStatus, nextStatus);
            if (combinedStatus != null) {
                float combinedAmount = current.amount() + next.amount();
                damageValues.add(new DamageValue(registryAccess.holderOrThrow(combinedStatus.getDamageType()), combinedAmount));
                i += 2;
            } else {
                damageValues.add(current);
                i++;
            }
        }

        return damageValues;
    }

    private static void addOrMergeType(List<DamageValue> orderedValues, DamageValue newValue) {
        for (int i = 0; i < orderedValues.size(); i++) {
            DamageValue value = orderedValues.get(i);
            if (value.damageType() == newValue.damageType()) {
                DamageValue damageValue = new DamageValue(newValue.damageType(), value.amount() + newValue.amount());
                orderedValues.set(i, damageValue);
                return;
            }
        }

        orderedValues.add(new DamageValue(newValue.damageType(), newValue.amount()));
    }

    public <E> ItemModifiers getItemModifiers(DataComponentType<List<ConditionalModEffect<E>>> componentType) {
        return this.itemModifiers.computeIfAbsent(componentType, dataComponent -> new ItemModifiers(new HashMap<>()));
    }

    public DamageModifiers getDamageModifiers(Holder<DamageType> damageType) {
        return this.damageModifiers.computeIfAbsent(damageType, type -> new DamageModifiers(new HashMap<>()));
    }
}
