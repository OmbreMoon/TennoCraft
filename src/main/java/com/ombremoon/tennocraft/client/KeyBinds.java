package com.ombremoon.tennocraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY_TC = "key.category.tennocraft.tc";
    public static final String KEY_ABILITY_ONE = "key.category.tennocraft.ability_one";
    public static final String KEY_ABILITY_TWO = "key.category.tennocraft.ability_two";
    public static final String KEY_ABILITY_THREE = "key.category.tennocraft.ability_three";
    public static final String KEY_ABILITY_FOUR = "key.category.tennocraft.ability_four";

    public static KeyMapping ABILITY_ONE_BINDING = new KeyMapping(KEY_ABILITY_ONE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_TC);
    public static KeyMapping ABILITY_TWO_BINDING = new KeyMapping(KEY_ABILITY_TWO, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_T, KEY_CATEGORY_TC);
    public static KeyMapping ABILITY_THREE_BINDING = new KeyMapping(KEY_ABILITY_THREE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KEY_CATEGORY_TC);
    public static KeyMapping ABILITY_FOUR_BINDING = new KeyMapping(KEY_ABILITY_FOUR, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_TC);
}
