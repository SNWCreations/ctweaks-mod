package snw.mods.ctweaks.mod.mixin.client;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snw.mods.ctweaks.protocol.packet.c2s.ServerboundWindowPropertiesPacket;

import static snw.lib.protocol.util.PacketHelper.newNonce;
import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;

@Mixin(Window.class)
public abstract class WindowMixin {

    @Shadow public abstract int getWidth();

    @Shadow public abstract int getHeight();

    @Shadow public abstract boolean isFullscreen();

    @Inject(method = "setMode", at = @At("TAIL"))
    private void resizeCallback(CallbackInfo ci) {
        getModC2SConnection().sendModPacket(() -> new ServerboundWindowPropertiesPacket(
                getWidth(),
                getHeight(),
                isFullscreen(),
                newNonce()
        ));
    }
}
