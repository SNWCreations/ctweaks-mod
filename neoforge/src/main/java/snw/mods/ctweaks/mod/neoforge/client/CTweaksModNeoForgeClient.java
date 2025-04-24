package snw.mods.ctweaks.mod.neoforge.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import snw.mods.ctweaks.mod.client.CTweaksModClient;

public final class CTweaksModNeoForgeClient {
    public static void init(IEventBus eventBus, ModContainer modContainer) {
        CTweaksModClient.init();

        eventBus.addListener(ForgeEvents::onRegisterPayloads);
    }
}
