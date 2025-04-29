package snw.mods.ctweaks.mod.mixin.client;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snw.mods.ctweaks.mod.client.net.ModC2SConnection;

@Getter
@Mixin(ClientPacketListener.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public class ClientPacketListenerMixin implements ModC2SConnection.Getter {
    @Unique
    private ModC2SConnection cTweaksModConnection;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        cTweaksModConnection = new ModC2SConnection((ClientPacketListener) (Object) this);
    }
}
