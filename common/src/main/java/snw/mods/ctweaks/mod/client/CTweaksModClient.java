package snw.mods.ctweaks.mod.client;

import lombok.extern.slf4j.Slf4j;
import snw.mods.ctweaks.mod.client.net.ClientNetHandler;

@Slf4j
public final class CTweaksModClient {
    public static void init() {
        log.info("Initializing CTweaks client features");
        ClientNetHandler.init();
    }
}
