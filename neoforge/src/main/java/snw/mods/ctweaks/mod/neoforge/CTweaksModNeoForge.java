package snw.mods.ctweaks.mod.neoforge;

import dev.architectury.utils.Env;
import snw.mods.ctweaks.mod.CTweaksMod;
import net.neoforged.fml.common.Mod;
import snw.mods.ctweaks.mod.neoforge.client.CTweaksModNeoForgeClient;

import static dev.architectury.utils.EnvExecutor.runInEnv;

@Mod(CTweaksMod.MOD_ID)
public final class CTweaksModNeoForge {
    public CTweaksModNeoForge() {
        // Run our common setup.
        CTweaksMod.init();
        runInEnv(Env.CLIENT, () -> CTweaksModNeoForgeClient::init);
    }
}
