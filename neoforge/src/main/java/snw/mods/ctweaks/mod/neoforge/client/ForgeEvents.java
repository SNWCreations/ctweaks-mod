package snw.mods.ctweaks.mod.neoforge.client;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import snw.mods.ctweaks.mod.client.net.ModPayload;

import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;

@Slf4j
public final class ForgeEvents {
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        log.info("Registering payloads");

        final PayloadRegistrar registrar = event.registrar("snwctweaks").optional();
        registrar.playBidirectional(ModPayload.TYPE, ModPayload.CODEC, (payload, ctx) -> {
            getModC2SConnection().handlePayload(payload);
        });
    }
}
