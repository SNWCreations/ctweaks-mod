package snw.mods.ctweaks.mod.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements snw.mods.ctweaks.mod.client.render.GuiGraphics {
    @Shadow @Final private Minecraft minecraft;

    @Override
    public Minecraft getMinecraft() {
        return minecraft;
    }

    @Shadow
    public abstract int drawString(Font font, Component text, int x, int y, int color);

    @Unique
    private GuiGraphics asHandle() {
        return (GuiGraphics) (Object) this;
    }

    @Override
    public void drawPlayerFace(ResourceLocation resourceLocation, int x, int y, int size) {
        PlayerFaceRenderer.draw(asHandle(), resourceLocation, x, y, size);
    }
}
