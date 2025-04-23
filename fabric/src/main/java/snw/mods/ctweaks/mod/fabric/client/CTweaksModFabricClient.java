package snw.mods.ctweaks.mod.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import snw.mods.ctweaks.mod.client.CTweaksModClient;

public final class CTweaksModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        CTweaksModClient.init();
    }
}
