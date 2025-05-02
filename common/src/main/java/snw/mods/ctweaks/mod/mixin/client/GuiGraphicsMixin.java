package snw.mods.ctweaks.mod.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
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
    public abstract int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow);

    @Override
    public int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow, float scale) {
        PoseStack poseStack = asHandle().pose();
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        int width = drawString(font, text, x, y, color, dropShadow);
        poseStack.popPose();
        return width;
    }

    @Override
    public void drawStringWithOutline(Font font, Component text, int x, int y, int color, int outlineColor, float scale) {
        PoseStack poseStack = asHandle().pose();
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        font.drawInBatch8xOutline(text.getVisualOrderText(), x, y, color, outlineColor, poseStack.last().pose(), asHandle().bufferSource(), 15728880);
        poseStack.popPose();
    }

    @Unique
    private GuiGraphics asHandle() {
        return (GuiGraphics) (Object) this;
    }

    @Override
    public void drawPlayerFace(ResourceLocation resourceLocation, int x, int y, int size) {
        PlayerFaceRenderer.draw(asHandle(), resourceLocation, x, y, size);
    }
}
