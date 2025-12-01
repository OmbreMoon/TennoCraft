package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public interface TCBullets {

    ResourceKey<Bullet> ATOMOS = key("atomos");

    static void bootstrap(BootstrapContext<Bullet> context) {
        register(
                context,
                ATOMOS,
                Bullet.builder()
                        .withModel(CommonClass.customLocation("geo/entity/test_bullet.geo.json"))
                        .withTexture(CommonClass.customLocation("textures/entity/test_bullet.png"))
        );
    }

    private static void register(BootstrapContext<Bullet> context, ResourceKey<Bullet> key, Bullet.Builder builder) {
        context.register(key, builder.build());
    }

    private static ResourceKey<Bullet> key(String name) {
        return ResourceKey.create(Keys.BULLET, CommonClass.customLocation(name));
    }
}
