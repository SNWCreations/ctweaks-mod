package snw.mods.ctweaks.mod.client.net;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;
import lombok.extern.slf4j.Slf4j;

import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;

@Slf4j
public final class ClientNetHandler {
    private ClientNetHandler() {
    }

    public static void init() {
        log.info("Registering network handlers");
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ModPayload.TYPE, ModPayload.CODEC,
                (payload, context) -> getModC2SConnection().handlePayload(payload));
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> getModC2SConnection().onConnected());
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> getModC2SConnection().onDisconnect());
    }
}
