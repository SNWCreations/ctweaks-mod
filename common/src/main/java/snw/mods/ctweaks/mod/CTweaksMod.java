package snw.mods.ctweaks.mod;

import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.architectury.platform.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CTweaksMod {
    public static final String MOD_ID = "snwctweaks";

    public static void init() {
        log.info("Initializing CTweaks Mod on Minecraft {}, {} platform", Platform.getMinecraftVersion(), ArchitecturyTarget.getCurrentTarget());
    }
}
