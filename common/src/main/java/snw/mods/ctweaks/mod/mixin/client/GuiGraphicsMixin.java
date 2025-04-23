package snw.mods.ctweaks.mod.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements snw.mods.ctweaks.mod.client.render.GuiGraphics {
    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
}
