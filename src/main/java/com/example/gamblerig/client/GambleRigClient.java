package com.example.gamblerig.client;

import com.example.gamblerig.TogglePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class GambleRigClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("gamblerig", "main"));
        KeyBinding menu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gamblerig.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, category));
        KeyBinding toggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gamblerig.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menu.wasPressed()) client.setScreen(new RigScreen());
            while (toggle.wasPressed() && client.player != null)
                ClientPlayNetworking.send(TogglePayload.INSTANCE);
        });
    }
}
