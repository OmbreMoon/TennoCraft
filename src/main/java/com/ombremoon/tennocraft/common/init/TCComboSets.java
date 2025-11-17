package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboType;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboValue;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public interface TCComboSets {
    ResourceKey<ComboSet> STAFF = key("staff");

    static void bootstrap(BootstrapContext<ComboSet> context) {
        register(
                context,
                STAFF,
                ComboSet.define()
                        .newCombo(
                                ComboType.NEUTRAL,
                                ComboValue.combo(
                                        CommonClass.customLocation("staff_auto_1"),
                                        ComboValue.withMultiplier(1.0F)
                                ),
                                ComboValue.combo(
                                        CommonClass.customLocation("staff_auto_2"),
                                        ComboValue.withMultiplier(1.0F)
                                ),
                                ComboValue.combo(
                                        CommonClass.customLocation("staff_auto_3"),
                                        ComboValue.withMultiplier(1.0F)
                                ),
                                ComboValue.combo(
                                        CommonClass.customLocation("staff_auto_4"),
                                        ComboValue.withMultiplier(1.0F)
                                )
                        )
        );
    }

    private static void register(BootstrapContext<ComboSet> context, ResourceKey<ComboSet> key, ComboSet.Builder builder) {
        context.register(key, builder.build());
    }

    private static ResourceKey<ComboSet> key(String name) {
        return ResourceKey.create(Keys.COMBO_SET, CommonClass.customLocation(name));
    }
}
