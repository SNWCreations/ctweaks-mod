package snw.mods.ctweaks.mod.client.net.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import snw.mods.ctweaks.mod.client.net.ModPayload;

import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;

public final class ClientNetHandlerImpl {
    private ClientNetHandlerImpl() {
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(ModPayload.TYPE, ModPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModPayload.TYPE, ModPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ModPayload.TYPE, (payload, ctx) -> {
            getModC2SConnection().handlePayload(payload);
        });
    }
}
