package snw.mods.ctweaks.mod.fabric;

import net.fabricmc.api.ModInitializer;
import snw.mods.ctweaks.mod.CTweaksMod;

public final class CTweaksModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CTweaksMod.init();
    }
}
