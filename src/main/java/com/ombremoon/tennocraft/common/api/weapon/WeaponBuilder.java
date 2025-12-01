package com.ombremoon.tennocraft.common.api.weapon;

import com.ombremoon.tennocraft.common.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.AutoTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.SlamAttack;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ZoomAttributes;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

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
        private List<ResourceLocation> triggerTypes = List.of(AutoTrigger.TYPE);

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

        public General triggerTypes(ResourceLocation... triggerTypes) {
            this.triggerTypes = Arrays.asList(triggerTypes);
            return this;
        }

        public Ranged ranged() {
            GeneralProperties general = new GeneralProperties(
                    this.slot,
                    this.mastery,
                    this.maxRank,
                    this.layout,
                    Optional.of(this.triggerTypes)
            );
            return new Ranged(general);
        }

        public Melee melee() {
            GeneralProperties general = new GeneralProperties(
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
        private final GeneralProperties general;
        private RangedUtilityProperties utility;
        private Map<TriggerType<?>, RangedAttackProperty> properties = new HashMap<>();

        Ranged(GeneralProperties general) {
            this.general = general;
        }

        public Ranged utility(int maxAmmo, int magSize, int ammoPickup, float rivenDisposition) {
            this.utility = new RangedUtilityProperties(maxAmmo, magSize, ammoPickup, rivenDisposition);
            return this;
        }

        public Ranged utility(int maxAmmo, int magSize, int ammoPickup, float rivenDisposition, ZoomAttributes info) {
            this.utility = new RangedUtilityProperties(maxAmmo, magSize, ammoPickup, rivenDisposition, Optional.of(info));
            return this;
        }

        public Ranged addAttack(TriggerType<?> triggerType, RangedAttackProperty schema) {
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
        private final GeneralProperties general;
        private MeleeUtilityProperties utility;
        private MeleeAttackProperties properties;

        Melee(GeneralProperties general) {
            this.general = general;
        }

        public Melee utility(float attackSpeed, int blockAngle, int comboDuration, float rivenDisposition, float followThrough) {
            this.utility = new MeleeUtilityProperties(attackSpeed, blockAngle, comboDuration, rivenDisposition, followThrough);
            return this;
        }

        public Melee attackProperties(Holder<ComboSet> combo, AttackProperty attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
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
