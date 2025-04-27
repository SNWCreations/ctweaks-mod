package snw.mods.ctweaks.mod.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.Component;

public final class AdventureHelper {
    private AdventureHelper() {
    }

    @ExpectPlatform
    public static Component asNative(net.kyori.adventure.text.Component adventure) {
        throw new AssertionError("Unhandled platform method");
    }
}
