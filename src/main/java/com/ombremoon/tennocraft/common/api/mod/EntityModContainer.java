package com.ombremoon.tennocraft.common.api.mod;

import com.ombremoon.tennocraft.common.api.IEntityModHolder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EntityModContainer extends ModContainer {

    public EntityModContainer(Modification.Compatibility type, Schema schema) {
        super(type, schema);
    }

    EntityModContainer(Modification.Compatibility type, Schema schema, boolean cache) {
        super(type, schema, cache);
    }

    @Override
    protected ModContainer createCache() {
        return new EntityModContainer(this.type, this.schema, true);
    }

    @Override
    public void confirmMods(Player player, IModHolder<?> holder, ItemStack stack) {
        this.collectModdedAttributes((IEntityModHolder<?>) holder);
        super.confirmMods(player, holder, stack);
    }


    public void collectModdedAttributes(IEntityModHolder<?> holder) {
        /*AttributeMap attributes = holder.getStats();
        ModHelper.forEachModifier(holder, (attributeHolder, attributeModifier) -> {
            AttributeInstance instance = attributes.getInstance(attributeHolder);
            if (instance != null) {
                instance.removeModifier(attributeModifier);
            }

            //Stop Location Based Effects
        });
        ModHelper.forEachModifier(holder, (attributeHolder, attributeModifier) -> {
            AttributeInstance instance = holder.getStats().getInstance(attributeHolder);
            if (instance != null) {
                instance.removeModifier(attributeModifier.id());
                instance.addPermanentModifier(attributeModifier);
            }

            //Run Location Changed Effects
        });*/
    }
}
