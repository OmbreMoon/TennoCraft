package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.tags.TagKey;

public class TCTags {

    public static class Schemas {
        public static final TagKey<Schema> RIFLE = tag("rifle");
        public static final TagKey<Schema> SHOTGUN = tag("shotgun");
        public static final TagKey<Schema> PISTOL = tag("pistol");

        private static TagKey<Schema> tag(String name) {
            return TagKey.create(Keys.SCHEMA, CommonClass.customLocation(name));
        }
    }
}
