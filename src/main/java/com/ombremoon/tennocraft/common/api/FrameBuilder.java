package com.ombremoon.tennocraft.common.api;

import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.util.IntPair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FrameBuilder implements SchemaBuilder<FrameSchema> {
    private final ObjectOpenHashSet<AbilityType<?>> abilities = new ObjectOpenHashSet<>();
    private Optional<AbilityType<?>> passive = Optional.empty();
    private Component description = Component.translatable("tennocraft.mineframe.missing_description");
    private final IntPair health = new IntPair(20, 1);
    private final IntPair shield = new IntPair(20, 1);
    private final IntPair energy = new IntPair(20, 1);
    private int armor;
    private int startEnergy;
    private float sprintSpeed;
    private ModLayout layout = new ModLayout(Modification.Compatibility.FRAME, List.of());

    FrameBuilder() {

    }

    public static FrameBuilder of() {
        return new FrameBuilder();
    }

    public FrameBuilder health(int health, int healthIncrease) {
        this.health.first(health);
        this.health.second(healthIncrease);
        return this;
    }

    public FrameBuilder health(int health) {
        this.health(health, 1);
        return this;
    }

    public FrameBuilder shield(int shield, int shieldIncrease) {
        this.shield.first(shield);
        this.shield.second(shieldIncrease);
        return this;
    }

    public FrameBuilder shield(int shield) {
        this.shield(shield, 1);
        return this;
    }

    public FrameBuilder energy(int energy, int energyIncrease) {
        this.energy.first(energy);
        this.energy.second(energyIncrease);
        return this;
    }

    public FrameBuilder energy(int energy) {
        this.energy(energy, 1);
        return this;
    }

    public final FrameBuilder abilities(AbilityType<?>... abilities) {
        this.abilities.addAll(Arrays.stream(abilities).toList());
        return this;
    }

    public FrameBuilder passive(AbilityType<?> passive) {
        this.passive = Optional.of(passive);
        return this;
    }

    public FrameBuilder armor(int armor) {
        this.armor = armor;
        return this;
    }

    public FrameBuilder startEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
        return this;
    }

    public FrameBuilder sprintSpeed(float speed) {
        this.sprintSpeed = speed;
        return this;
    }

    public FrameBuilder description(Component description) {
        this.description = description;
        return this;
    }

    public FrameBuilder layout(Modification.Compatibility compatibility, ModSlot... slots) {
        this.layout = new ModLayout(compatibility, Arrays.asList(slots));
        return this;
    }

    public FrameSchema build() {
        return new FrameSchema(
                this.abilities,
                this.passive,
                this.description,
                this.health,
                this.shield,
                this.energy,
                this.armor,
                this.startEnergy,
                this.sprintSpeed,
                this.layout
        );
    }
}
