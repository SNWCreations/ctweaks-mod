package snw.mods.ctweaks.mod.client.net.fabric;

import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import snw.mods.ctweaks.mod.client.net.ModPayload;

import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;

@Slf4j
public final class ClientNetHandlerImpl {
    private ClientNetHandlerImpl() {
    }

    public static void registerPayloads() {
        log.info("Registering payloads");
        PayloadTypeRegistry.playS2C().register(ModPayload.TYPE, ModPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModPayload.TYPE, ModPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ModPayload.TYPE, (payload, ctx) -> {
            getModC2SConnection().handlePayload(payload);
        });
    }
}
