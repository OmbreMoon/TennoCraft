package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.FrameBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCAbilities;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface TCFrames {

    ResourceKey<Schema> VOLT = key("volt");

    static void bootstrap(BootstrapContext<Schema> context) {
        register(
                context,
                VOLT,
                FrameBuilder.of()
                        .abilities(TCAbilities.TEST.value())
                        .description(Component.literal("volt"))
        );
    }

    private static void register(BootstrapContext<Schema> context, ResourceKey<Schema> key, FrameBuilder builder) {
        context.register(key, builder.build());
        Schemas.SCHEMAS.add(key);
    }

    private static ResourceKey<Schema> key(String name) {
        return ResourceKey.create(Keys.SCHEMA, CommonClass.customLocation("frame/" + name));
    }
}
