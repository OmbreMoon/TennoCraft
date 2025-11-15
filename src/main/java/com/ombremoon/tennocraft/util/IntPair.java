package com.ombremoon.tennocraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class IntPair implements IntIntPair {
    public static final Codec<IntPair> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("first").forGetter(intPair -> intPair.first),
                    Codec.INT.fieldOf("second").forGetter(intPair -> intPair.second)
            ).apply(instance, IntPair::new)
    );
    public static final StreamCodec<FriendlyByteBuf, IntPair> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, IntIntPair::leftInt,
            ByteBufCodecs.VAR_INT, IntIntPair::rightInt,
            IntPair::new
    );
    protected int first;
    protected int second;

    public IntPair(final int first, final int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int leftInt() {
        return this.first;
    }

    @Override
    public int rightInt() {
        return this.second;
    }

    /**
     * Returns a new type-specific mutable {@link it.unimi.dsi.fastutil.Pair Pair} with given left and
     * right value.
     *
     * @param first the left value.
     * @param second the right value.
     *
     * @implSpec This factory method delegates to the constructor.
     */
    public static IntPair of(final int first, final int second) {
        return new IntPair(first, second);
    }


    @Override
    public IntPair left(final int second) {
        this.second = second;
        return this;
    }

    @Override
    public IntPair right(final int first) {
        this.first = first;
        return this;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof IntIntPair) {
            return this.first == ((IntIntPair)other).leftInt() && this.second == ((IntIntPair)other).rightInt();
        }
        if (other instanceof it.unimi.dsi.fastutil.Pair) {
            return java.util.Objects.equals(Integer.valueOf(this.first), ((it.unimi.dsi.fastutil.Pair)other).left()) && java.util.Objects.equals(Integer.valueOf(this.second), ((it.unimi.dsi.fastutil.Pair)other).right());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.first) * 19 + (this.second);
    }

    @Override
    public String toString() {
        return "<" + leftInt() + "," + rightInt() + ">";
    }
}
