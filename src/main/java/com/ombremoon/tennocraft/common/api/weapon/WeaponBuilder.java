package com.ombremoon.tennocraft.common.api.weapon;

import com.ombremoon.tennocraft.common.api.weapon.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.projectile.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.schema.*;
import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.*;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;

import java.util.*;

public abstract class WeaponBuilder {

    WeaponBuilder() {
    }

    public static General of() {
        return new General();
    }

    public static class General extends WeaponBuilder {
        private WeaponSlot slot = WeaponSlot.PRIMARY;
        private int mastery;
        private int maxRank = 30;
        private ModLayout layout = new ModLayout(Modification.Compatibility.PRIMARY, List.of());
        private List<TriggerType> triggerTypes = List.of(TriggerType.AUTO);

        public General slot(WeaponSlot slot) {
            this.slot = slot;
            return this;
        }

        public General mastery(int mastery) {
            this.mastery = mastery;
            return this;
        }

        public General maxRank(int maxRank) {
            this.maxRank = maxRank;
            return this;
        }

        public General layout(Modification.Compatibility compatibility, ModSlot... slots) {
            this.layout = new ModLayout(compatibility, Arrays.asList(slots));
            return this;
        }

        public General triggerTypes(TriggerType... triggerTypes) {
            this.triggerTypes = Arrays.asList(triggerTypes);
            return this;
        }

        public Ranged ranged() {
            GeneralSchema general = new GeneralSchema(
                    this.slot,
                    this.mastery,
                    this.maxRank,
                    this.layout,
                    Optional.of(this.triggerTypes)
            );
            return new Ranged(general);
        }

        public Melee melee() {
            GeneralSchema general = new GeneralSchema(
                    this.slot,
                    this.mastery,
                    this.maxRank,
                    this.layout,
                    Optional.empty()
            );
            return new Melee(general);
        }
    }

    public static class Ranged extends WeaponBuilder {
        private final GeneralSchema general;
        private RangedUtilitySchema utility;
        private Map<TriggerType, RangedAttackSchema> properties = new HashMap<>();

        Ranged(GeneralSchema general) {
            this.general = general;
        }

        public Ranged utility(float accuracy, float fireRate, int maxAmmo, int magSize, int ammoPickup, int reloadTime, float rivenDisposition) {
            this.utility = new RangedUtilitySchema(accuracy, fireRate, maxAmmo, magSize, ammoPickup, reloadTime, rivenDisposition);
            return this;
        }

        public Ranged utility(float accuracy, float fireRate, int maxAmmo, int magSize, int ammoPickup, int reloadTime, float rivenDisposition, SniperInfo info) {
            this.utility = new RangedUtilitySchema(accuracy, fireRate, maxAmmo, magSize, ammoPickup, reloadTime, rivenDisposition, Optional.of(info));
            return this;
        }

        public Ranged addAttack(TriggerType triggerType, RangedAttackSchema schema) {
            this.properties.put(triggerType, schema);
            return this;
        }

        public RangedWeaponSchema build() {
            if (this.utility == null)
                throw new IllegalArgumentException("Invalid weapon schema. Utility schema is undefined.");

            if (this.properties.isEmpty())
                throw new IllegalArgumentException("Invalid weapon schema. Attack properties are undefined.");

            return new RangedWeaponSchema(this.general, this.utility, new RangedAttackProperties(this.properties));
        }
    }

    public static class Melee extends WeaponBuilder {
        private final GeneralSchema general;
        private MeleeUtilitySchema utility;
        private MeleeAttackProperties properties;

        Melee(GeneralSchema general) {
            this.general = general;
        }

        public Melee utility(float attackSpeed, int blockAngle, int comboDuration, float rivenDisposition, float followThrough) {
            this.utility = new MeleeUtilitySchema(attackSpeed, blockAngle, comboDuration, rivenDisposition, followThrough);
            return this;
        }

        public Melee attackProperties(Holder<ComboSet> combo, AttackSchema attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
            this.properties = new MeleeAttackProperties(combo, attack, windUp, slamAttack, heavySlamAttack);
            return this;
        }

        public MeleeWeaponSchema build() {
            if (this.utility == null)
                throw new IllegalArgumentException("Invalid weapon schema. Utility schema is undefined.");

            if (this.properties == null)
                throw new IllegalArgumentException("Invalid weapon schema. Attack properties are undefined.");

            return new MeleeWeaponSchema(this.general, this.utility, this.properties);
        }
    }
}
