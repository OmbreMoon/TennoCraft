package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.object.item.weapon.MeleeWeapon;
import com.ombremoon.tennocraft.object.item.weapon.SecondaryWeapon;
import com.ombremoon.tennocraft.player.weapon.WeaponProperties;
import com.ombremoon.tennocraft.player.weapon.WeaponType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCWeapons {
    public static void init() {}

    //SECONDARY
    public static final RegistryObject<Item> LATO = registerSecondaryWeapon("lato", new Item.Properties(),
            new WeaponProperties().weaponType(WeaponType.SEMIAUTO)
                    .impactDamage(5).punctureDamage(5).slashDamage(10).statusChance(0.06F));

    public static final RegistryObject<Item> ANGSTRUM = registerSecondaryWeapon("angstrum", new Item.Properties(),
            new WeaponProperties().weaponType(WeaponType.CHARGE)
                    .blastDamage(100).statusChance(0.22F));

    //MELEE
    public static final RegistryObject<Item> SKANA = registerMeleeWeapon("skana", new Item.Properties(),
            new WeaponProperties().weaponType(WeaponType.MELEE)
                    .impactDamage(9).punctureDamage(9).slashDamage(42));

    private static <T extends Item> RegistryObject<T> registerWeaponItem(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }

    private static RegistryObject<Item> registerSecondaryWeapon(String name, Item.Properties properties, WeaponProperties weaponProperties) {
        return registerWeaponItem(name, () -> new SecondaryWeapon(properties, weaponProperties));
    }

    private static RegistryObject<Item> registerMeleeWeapon(String name, Item.Properties properties, WeaponProperties weaponProperties) {
        return registerWeaponItem(name, () -> new MeleeWeapon(properties, weaponProperties));
    }
}
