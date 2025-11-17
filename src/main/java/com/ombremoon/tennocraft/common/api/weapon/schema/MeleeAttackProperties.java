package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.SlamAttack;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public record MeleeAttackProperties(ResourceKey<ComboSet> combo, AttackSchema attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
    public static final Codec<MeleeAttackProperties> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceKey.codec(Keys.COMBO_SET).fieldOf("combo").forGetter(MeleeAttackProperties::combo),
                    AttackSchema.CODEC.fieldOf("attack").forGetter(MeleeAttackProperties::attack),
                    Codec.INT.fieldOf("windUp").forGetter(MeleeAttackProperties::windUp),
                    SlamAttack.CODEC.fieldOf("slamAttack").forGetter(MeleeAttackProperties::slamAttack),
                    SlamAttack.CODEC.fieldOf("heavySlamAttack").forGetter(MeleeAttackProperties::heavySlamAttack)
            ).apply(instance, MeleeAttackProperties::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MeleeAttackProperties> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Keys.COMBO_SET), MeleeAttackProperties::combo,
            AttackSchema.STREAM_CODEC, MeleeAttackProperties::attack,
            ByteBufCodecs.INT, MeleeAttackProperties::windUp,
            SlamAttack.STREAM_CODEC, MeleeAttackProperties::slamAttack,
            SlamAttack.STREAM_CODEC, MeleeAttackProperties::heavySlamAttack,
            MeleeAttackProperties::new
    );
}
