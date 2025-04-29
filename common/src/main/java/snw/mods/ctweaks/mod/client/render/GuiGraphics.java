package snw.mods.ctweaks.mod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface GuiGraphics {
    Minecraft getMinecraft();

    int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow, float scale);

    void drawPlayerFace(ResourceLocation resourceLocation, int x, int y, int size);
}
