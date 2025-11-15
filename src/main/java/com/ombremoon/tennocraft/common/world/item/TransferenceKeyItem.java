package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.modholder.api.FrameSchema;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.world.SchemaHolder;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TransferenceKeyItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TransferenceKeyItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public Component getName(ItemStack stack) {
        SchemaHolder schema = stack.get(TCData.SCHEMA);
        if (schema != null) {
            ResourceLocation id = schema.location();
            String namespace = id.getNamespace();
            String[] name = id.getPath().split("/");
            return Component.translatable(namespace + "." + name[0] + "." + name[1]).append(" ").append(super.getName(stack));
        }

        return super.getName(stack);
    }

    public static ItemStack createWithFrame(SchemaHolder schema) {
        ItemStack key = new ItemStack(TCItems.TRANSFERENCE_KEY.get());
        key.set(TCData.SCHEMA, schema);
        return key;
    }

    public static ItemStack createWithFrame(Level level, ResourceKey<Schema> key) {
        Schema schema = level.registryAccess().registryOrThrow(Keys.SCHEMA).getOrThrow(key);
        if (schema instanceof FrameSchema) {
            var holder = new SchemaHolder(key, schema, "frame");
            return createWithFrame(holder);
        }

        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
