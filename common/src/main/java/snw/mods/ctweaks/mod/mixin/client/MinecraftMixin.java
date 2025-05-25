package snw.mods.ctweaks.mod.mixin.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snw.mods.ctweaks.mod.client.ClientWindow;
import snw.mods.ctweaks.mod.client.net.ModC2SConnection;
import snw.mods.ctweaks.protocol.packet.c2s.ServerboundWindowPropertiesPacket;

import static snw.lib.protocol.util.PacketHelper.newNonce;
import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnectionOrNull;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements ClientWindow {
    @Shadow public abstract Window getWindow();

    @Unique private boolean windowStateDirty;

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    private void onResizeDisplay(CallbackInfo ci) {
        windowStateDirty = true;
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;updateDisplay()V"))
    private void afterUpdateDisplay(boolean renderLevel, CallbackInfo ci) {
        if (windowStateDirty) {
            sendWindowProperties();
            windowStateDirty = false;
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void sendWindowProperties() {
        ModC2SConnection modC2SConnection = getModC2SConnectionOrNull();
        if (modC2SConnection != null) {
            modC2SConnection.sendModPacket(() -> new ServerboundWindowPropertiesPacket(
                    getWindow().getGuiScaledWidth(),
                    getWindow().getGuiScaledHeight(),
                    getWindow().isFullscreen(),
                    newNonce()
            ));
        }
    }
}
