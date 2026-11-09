package com.example.gamblerig.client;

import com.example.gamblerig.ConfigPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.List;

public class RigScreen extends Screen {
    private static final List<Item> ITEMS = List.of(
        Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, Items.DIAMOND,
        Items.PAPER, Items.OBSIDIAN, Items.COAL);
    private static int itemIndex = 0;
    private static boolean win = true;

    public RigScreen() { super(Text.literal("Gamble Rig")); }

    @Override
    protected void init() {
        int cx = width / 2 - 100, y = height / 2 - 30;

        addDrawableChild(ButtonWidget.builder(itemLabel(), b -> {
            itemIndex = (itemIndex + 1) % ITEMS.size();
            b.setMessage(itemLabel());
        }).dimensions(cx, y, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(modeLabel(), b -> {
            win = !win;
            b.setMessage(modeLabel());
        }).dimensions(cx, y + 26, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Apply"), b -> {
            ClientPlayNetworking.send(new ConfigPayload(win, Registries.ITEM.getId(ITEMS.get(itemIndex))));
            close();
        }).dimensions(cx, y + 52, 200, 20).build());
    }

    private Text itemLabel() { return Text.literal("Item: ").append(ITEMS.get(itemIndex).getName()); }
    private Text modeLabel() { return Text.literal("Outcome: " + (win ? "Force WIN" : "Force LOSE")); }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx, mx, my, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 55, 0xFFFFFFFF);
    }
}
