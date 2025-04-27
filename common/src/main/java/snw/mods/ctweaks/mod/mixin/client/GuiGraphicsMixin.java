package snw.mods.ctweaks.mod.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements snw.mods.ctweaks.mod.client.render.GuiGraphics {
    @Shadow @Final private Minecraft minecraft;

    @Override
    public Minecraft getMinecraft() {
        return minecraft;
    }
}
