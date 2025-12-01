package com.ombremoon.tennocraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY = "key.category." + Constants.MOD_ID;
    public static final String KEY_ALTERNATE_FIRE = "key." + Constants.MOD_ID + ".alternate_fire";

    public static final KeyMapping ALTERNATE_FIRE_BINDING = new KeyMapping(KEY_ALTERNATE_FIRE, KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_3, KEY_CATEGORY);

    public static KeyMapping getShootMapping() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.options.keyAttack;
    }
}
