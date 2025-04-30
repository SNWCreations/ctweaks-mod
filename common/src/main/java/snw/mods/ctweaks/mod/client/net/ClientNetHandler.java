package snw.mods.ctweaks.mod.client.net;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.jetbrains.annotations.Nullable;

import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;
import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnectionOrNull;

@Slf4j
public final class ClientNetHandler {
    private ClientNetHandler() {
    }

    public static void init() {
        log.info("Registering network handlers");
        registerPayloads();
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> getModC2SConnection().onConnected());
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            ModC2SConnection modC2SConnection = getModC2SConnectionOrNull();
            if (modC2SConnection != null) {
                modC2SConnection.onDisconnect();
            }
        });
    }

    @ExpectPlatform
    public static void registerPayloads() {
        throw new IllegalStateException("Unhandled platform method");
    }
}
