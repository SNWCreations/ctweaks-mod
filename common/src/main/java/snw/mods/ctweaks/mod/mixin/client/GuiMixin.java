package snw.mods.ctweaks.mod.mixin.client;

import lombok.Getter;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snw.mods.ctweaks.mod.client.render.ModRender;

@Getter
@Mixin(Gui.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public class GuiMixin implements ModRender.Getter {
    @Unique
    private ModRender cTweaksModRender;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(Minecraft minecraft, CallbackInfo ci) {
        cTweaksModRender = new ModRender();
    }

    @Inject(method = "render", at= @At("TAIL"))
    private void afterRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        cTweaksModRender.render((snw.mods.ctweaks.mod.client.render.GuiGraphics) guiGraphics);
    }
}
