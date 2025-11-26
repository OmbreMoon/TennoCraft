package com.ombremoon.tennocraft.common.api.mod.effects;

import com.google.common.collect.HashMultimap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IEntityModHolder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.RankBasedValue;
import net.minecraft.client.model.EntityModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public record ModAttributeEffect(ResourceLocation id, Holder<Attribute> attribute, RankBasedValue amount, AttributeModifier.Operation operation) implements ModLocationEffect {
    public static final MapCodec<ModAttributeEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModAttributeEffect::id),
                    Attribute.CODEC.fieldOf("attribute").forGetter(ModAttributeEffect::attribute),
                    RankBasedValue.CODEC.fieldOf("maxFalloff").forGetter(ModAttributeEffect::amount),
                    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(ModAttributeEffect::operation)
            ).apply(instance, ModAttributeEffect::new)
    );

    public AttributeModifier getModifier(int rank) {
        return new AttributeModifier(this.id, this.amount.calculate(rank), this.operation);
    }

    @Override
    public void onChangedBlock(ServerLevel level, int modRank, IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        if (modHolder instanceof IEntityModHolder<?> entityModHolder)
            entityModHolder.getStats().addTransientAttributeModifiers(this.makeAttributeMap(modRank));
    }

    @Override
    public void onDeactivated(IModHolder<?> modHolder, ItemStack stack, LivingEntity attacker, Entity entity, Vec3 pos, int modRank) {
        if (modHolder instanceof IEntityModHolder<?> entityModHolder)
            entityModHolder.getStats().removeAttributeModifiers(this.makeAttributeMap(modRank));
    }

    private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int rank) {
        HashMultimap<Holder<Attribute>, AttributeModifier> hashmultimap = HashMultimap.create();
        hashmultimap.put(this.attribute, this.getModifier(rank));
        return hashmultimap;
    }

    @Override
    public MapCodec<? extends ModLocationEffect> codec() {
        return CODEC;
    }
}
