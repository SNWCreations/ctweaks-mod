package snw.mods.ctweaks.mod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public interface GuiGraphics {
    Minecraft getMinecraft();

    int drawString(Font font, Component text, int x, int y, int color);
}
