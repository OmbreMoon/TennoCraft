package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.SlamAttack;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public record MeleeAttackProperties(Holder<ComboSet> combo, AttackProperty attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
    public static final Codec<MeleeAttackProperties> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ComboSet.CODEC.fieldOf("combo").forGetter(MeleeAttackProperties::combo),
                    AttackProperty.CODEC.fieldOf("attack").forGetter(MeleeAttackProperties::attack),
                    Codec.INT.fieldOf("windUp").forGetter(MeleeAttackProperties::windUp),
                    SlamAttack.CODEC.fieldOf("slamAttack").forGetter(MeleeAttackProperties::slamAttack),
                    SlamAttack.CODEC.fieldOf("heavySlamAttack").forGetter(MeleeAttackProperties::heavySlamAttack)
            ).apply(instance, MeleeAttackProperties::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MeleeAttackProperties> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MeleeAttackProperties decode(RegistryFriendlyByteBuf buffer) {
            ResourceKey<ComboSet> key = ResourceKey.streamCodec(Keys.COMBO_SET).decode(buffer);
            AttackProperty attacks = AttackProperty.STREAM_CODEC.decode(buffer);
            int windUp = buffer.readVarInt();
            SlamAttack slamAttack = SlamAttack.STREAM_CODEC.decode(buffer);
            SlamAttack heavySlamAttack = SlamAttack.STREAM_CODEC.decode(buffer);
            Holder<ComboSet> comboSet = buffer.registryAccess().registryOrThrow(Keys.COMBO_SET).getHolderOrThrow(key);
            return new MeleeAttackProperties(comboSet, attacks, windUp, slamAttack, heavySlamAttack);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, MeleeAttackProperties value) {
            ResourceKey<ComboSet> key = value.combo.unwrapKey().orElseThrow();
            ResourceKey.streamCodec(Keys.COMBO_SET).encode(buffer, key);
            AttackProperty.STREAM_CODEC.encode(buffer, value.attack);
            buffer.writeVarInt(value.windUp);
            SlamAttack.STREAM_CODEC.encode(buffer, value.slamAttack);
            SlamAttack.STREAM_CODEC.encode(buffer, value.heavySlamAttack);
        }
    };
}
