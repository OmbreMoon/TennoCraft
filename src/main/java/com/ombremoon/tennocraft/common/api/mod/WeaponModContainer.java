package com.ombremoon.tennocraft.common.api.mod;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.util.IntPair;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class WeaponModContainer extends ModContainer {
    private final WeaponSchema schema;
    public final List<Holder<DamageType>> damageModifiers = new ObjectArrayList<>();
    private final Map<DataComponentType<?>, ItemModifiers> itemModifiers;

    /**
     * Collect item modifiers when...
     * 1) Switching modholders
     * 2) Confirming mod containers
     */

    public WeaponModContainer(Modification.Compatibility type, WeaponSchema schema) {
        super(type, schema);
        this.schema = schema;
        this.itemModifiers = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public void confirmMods(Level level, IModHolder<?> holder, ItemStack stack) {
//        this.collectItemModifiers();
        super.confirmMods(level, holder, stack);
    }

    public void collectItemModifiers(ItemModifiers oldModifiers, ItemModifiers newModifiers) {

    }

    public <E> ItemModifiers getItemModifiers(DataComponentType<List<ConditionalModEffect<E>>> componentType) {
        return this.itemModifiers.computeIfAbsent(componentType, dataComponent -> new ItemModifiers(new HashMap<>()));
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = super.serializeNBT(provider);
        ListTag modifierList = new ListTag();
        for (var modifier : this.damageModifiers) {
            CompoundTag tag = new CompoundTag();
            ResourceKey<DamageType> resourcekey = modifier.getKey();
            tag.putString("DamageType", resourcekey.location().toString());
            modifierList.add(tag);
        }
        compoundTag.put("Damage Modifiers", modifierList);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        if (nbt.contains("Damage Modifiers", 9)) {
            ListTag listTag = nbt.getList("Damage Modifiers", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                ResourceKey<DamageType> damageType = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(compoundTag.getString("DamageType")));
                this.damageModifiers.add(provider.holderOrThrow(damageType));
            }
        }
    }

    public record ItemModifiers(Map<ResourceLocation, Pair<ConditionalModEffect<ModValueEffect>, Integer>> modifiers) {

        public void put(ResourceLocation id, int modRank, ConditionalModEffect<ModValueEffect> effect) {
            this.modifiers.put(id, Pair.of(effect, modRank));
        }

        public void remove(ResourceLocation id) {
            this.modifiers.remove(id);
        }

        public void forEach(BiConsumer<ResourceLocation, Pair<ConditionalModEffect<ModValueEffect>, Integer>> consumer) {
            this.modifiers.forEach(consumer);
        }
    }
}
