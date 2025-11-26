package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.client.particle.BulletFX;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.*;

public record Bullet(Optional<ResourceLocation> modelLocation, Optional<ResourceLocation> textureLocation, Optional<List<BulletFX>> vfx, DataComponentMap effects) {
    public static final Codec<Bullet> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("model").forGetter(Bullet::modelLocation),
                    ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(Bullet::textureLocation),
                    BulletFX.CODEC.listOf().optionalFieldOf("vfx").forGetter(Bullet::vfx),
                    DataComponentMap.CODEC.fieldOf("effects").forGetter(Bullet::effects)
            ).apply(instance, Bullet::new)
    );
    public static Codec<Holder<Bullet>> CODEC = RegistryFileCodec.create(Keys.BULLET, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Bullet>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Keys.BULLET);

    public void runEffects(Entity entity, BulletFX.AttachmentPoint point) {
        if (vfx.isPresent()) {
            for (BulletFX effect : this.vfx.get()) {
                if (effect.attachTo() == point) {
                    this.makeEffect(entity, effect);
                }
            }
        }
    }

    public EntityEffectExecutor makeEffect(Entity entity, BulletFX effect) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            var fx = FXHelper.getFX(effect.id());
            if (fx != null) {
                var executor = new EntityEffectExecutor(fx, level, entity, entity instanceof BulletProjectile ? EntityEffectExecutor.AutoRotate.LOOK : EntityEffectExecutor.AutoRotate.NONE);
                effect.offset().ifPresent(vec3 -> executor.setOffset(vec3.toVector3f()));
                effect.rotation().ifPresent(vec3 -> executor.setRotation(vec3.x, vec3.y, vec3.z));
                effect.rotation().ifPresent(vec3 -> executor.setScale(vec3.x, vec3.y, vec3.z));
                executor.setDelay(effect.delay());
                executor.setAllowMulti(true);
                return executor;
            }
        }

        return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
        private List<BulletFX> vfxList;
        private ResourceLocation modelLocation;
        private ResourceLocation textureLocation;

        public Builder withModel(ResourceLocation location) {
            this.modelLocation = location;
            return this;
        }

        public Builder withTexture(ResourceLocation location) {
            this.textureLocation = location;
            return this;
        }

        public Builder withVFX(BulletFX vfx) {
            if (this.vfxList == null)
                this.vfxList = new ArrayList<>();

            this.vfxList.add(vfx);
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, LootItemCondition.Builder requirements) {
            ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.empty());
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.of(conditions)));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.empty()));
            return this;
        }

        @SuppressWarnings("unchecked")
        private <E> List<E> getEffectsList(DataComponentType<List<E>> componentType) {
            return (List<E>) this.effectLists.computeIfAbsent(componentType, dataComponent -> {
                ArrayList<E> arrayList = new ArrayList<>();
                this.effectMapBuilder.set(componentType, arrayList);
                return arrayList;
            });
        }

        public Bullet build() {
            return new Bullet(Optional.ofNullable(this.modelLocation), Optional.ofNullable(this.textureLocation), Optional.ofNullable(this.vfxList), this.effectMapBuilder.build());
        }
    }
}
