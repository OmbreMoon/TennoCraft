package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import com.ombremoon.tennocraft.common.api.IModHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record ModifyItemValue(Modification.Compatibility compatibility, DataComponentType<?> componentType, ModValueEffect effect) implements ModEntityEffect {
    public static final MapCodec<ModifyItemValue> CODEC = RecordCodecBuilder.<ModifyItemValue>mapCodec(
            instance -> instance.group(
                    Modification.Compatibility.CODEC.fieldOf("weapon").forGetter(ModifyItemValue::compatibility),
                    TCModEffectComponents.COMPONENT_CODEC.fieldOf("type").forGetter(ModifyItemValue::componentType),
                    ModValueEffect.CODEC.fieldOf("effect").forGetter(ModifyItemValue::effect)
            ).apply(instance, ModifyItemValue::new)
    ).validate(ModifyItemValue::validate);

    private static DataResult<ModifyItemValue> validate(ModifyItemValue modifyItemValue) {
        return !modifyItemValue.compatibility.isWeapon() ? DataResult.error(() -> "Encountered non-weapon mod compatibility: " + modifyItemValue.compatibility) : DataResult.success(modifyItemValue);
    }

    public static ModifyItemValue modifyValue(Modification.Compatibility compatibility, DataComponentType<List<ConditionalModEffect<ModValueEffect>>> type, ModValueEffect effect) {
        return new ModifyItemValue(compatibility, type, effect);
    }

    @Override
    public void apply(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, Entity entity, Vec3 origin) {
        DataComponentType<List<ModValueEffect>> type = (DataComponentType<List<ModValueEffect>>) this.componentType;
    }

    @Override
    public MapCodec<? extends ModEntityEffect> codec() {
        return CODEC;
    }
}
