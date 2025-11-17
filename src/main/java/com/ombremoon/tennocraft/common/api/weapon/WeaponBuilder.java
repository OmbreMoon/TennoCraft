package com.ombremoon.tennocraft.common.api.weapon;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        private final RangedAttackProperties properties = new RangedAttackProperties(new Object2ObjectOpenHashMap<>());

        Ranged(GeneralSchema general) {
            this.general = general;
        }

        public Utility utility() {
            return new Utility(this);
        }

        public Attack attack(TriggerType type) {
            return new Attack(this, type);
        }

        public RangedWeaponSchema build() {
            if (this.utility == null)
                throw new IllegalArgumentException("Invalid weapon schema. Utility schema is undefined.");

            if (this.properties.isEmpty())
                throw new IllegalStateException("Invalid weapon schema. Must have at least one attack schema defined.");

            return new RangedWeaponSchema(this.general, this.utility, this.properties);
        }

        public static class Utility {
            private final Ranged ranged;
            private Accuracy accuracy = Accuracy.MEDIUM;
            private float fireRate;
            private int maxAmmo;
            private int magSize;
            private int ammoPickup;
            private float reloadTime;
            private float rivenDisposition = 1.0F;

            Utility(Ranged ranged) {
                this.ranged = ranged;
            }

            public Utility accuracy(Accuracy accuracy) {
                this.accuracy = accuracy;
                return this;
            }

            public Utility fireRate(float fireRate) {
                this.fireRate = fireRate;
                return this;
            }

            public Utility maxAmmo(int maxAmmo) {
                this.maxAmmo = maxAmmo;
                return this;
            }

            public Utility magSize(int magSize) {
                this.magSize = magSize;
                return this;
            }

            public Utility ammoPickup(int ammoPickup) {
                this.ammoPickup = ammoPickup;
                return this;
            }

            public Utility reloadTime(float reloadTime) {
                this.reloadTime = reloadTime;
                return this;
            }

            public Utility rivenDisposition(float rivenDisposition) {
                this.rivenDisposition = rivenDisposition;
                return this;
            }

            public Ranged buildUtility() {
                ranged.utility = new RangedUtilitySchema(
                        this.accuracy,
                        this.fireRate,
                        this.maxAmmo,
                        this.magSize,
                        this.ammoPickup,
                        this.reloadTime,
                        this.rivenDisposition
                );
                return this.ranged;
            }
        }

        public static class Attack {
            private final Ranged ranged;
            private final TriggerType type;
            private final List<RangedAttackSchema> attacks = new ObjectArrayList<>();
            private RangedAttack rangedAttack;
            private List<DamageValue> damage = new ObjectArrayList<>();
            private float critChance;
            private float critMultiplier;
            private float status;
            private NoiseLevel noise = NoiseLevel.ALARMING;


            Attack(Ranged ranged, TriggerType type) {
                this.ranged = ranged;
                this.type = type;
            }

            public Attack withDamage(DamageValue... damageValues) {
                this.damage = Arrays.asList(damageValues);
                return this;
            }

            public Attack critChance(float critChance) {
                this.critChance = critChance;
                return this;
            }

            public Attack critMultiplier(float critMultiplier) {
                this.critMultiplier = critMultiplier;
                return this;
            }

            public Attack status(float status) {
                this.status = status;
                return this;
            }

            public Attack noiseLevel(NoiseLevel noise) {
                this.noise = noise;
                return this;
            }

            public Data withData() {
                return new Data(this);
            }

            private Attack addAttack() {
                if (this.rangedAttack == null)
                    throw new IllegalArgumentException("Invalid attack schema. Ranged attack is undefined.");

                RangedAttackSchema schema = new RangedAttackSchema(
                        this.rangedAttack,
                        this.damage,
                        this.critChance,
                        this.critMultiplier,
                        this.status,
                        this.noise
                );
                this.attacks.add(schema);
                this.rangedAttack = null;
                return this;
            }

            public Ranged buildAttack() {
                if (this.attacks.isEmpty())
                    throw new IllegalStateException("Invalid attack schema. Cannot have empty schema.");

                ranged.properties.addProperty(this.type, this.attacks);
                return this.ranged;
            }

            public static class Data {
                private final Attack attack;
                private int ammoCost;
                private float fireRate;
                private int multiShot;
                private float punchThrough;
                private UniformInt spread = UniformInt.of(2, 6);
                private float projectileSpeed;
                private ProjectileType<?> type = new Hitscan(300);
                private Optional<ResourceKey<DamageType>> forcedProc = Optional.empty();
                private Optional<BurstData> burst = Optional.empty();

                Data(Attack attack) {
                    this.attack = attack;
                }

                public Attack createAttack() {
                    this.attack.rangedAttack = new RangedAttack(
                            this.ammoCost,
                            this.fireRate,
                            this.multiShot,
                            this.punchThrough,
                            this.spread,
                            this.projectileSpeed,
                            this.type,
                            this.forcedProc,
                            this.burst
                    );
                    return this.attack.addAttack();
                }
            }
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

        public Melee attackProperties(ResourceKey<ComboSet> combo, AttackSchema attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
            this.properties = new MeleeAttackProperties(combo, attack, windUp, slamAttack, heavySlamAttack);
            return this;
        }

        public Melee attackProperties(Holder<ComboSet> combo, AttackSchema attack, int windUp, SlamAttack slamAttack, SlamAttack heavySlamAttack) {
            return this.attackProperties(combo.getKey(), attack, windUp, slamAttack, heavySlamAttack);
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
