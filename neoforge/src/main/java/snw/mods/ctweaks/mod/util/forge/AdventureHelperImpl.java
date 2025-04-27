package snw.mods.ctweaks.mod.util.forge;

import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.minecraft.network.chat.Component;

public final class AdventureHelperImpl {
    private AdventureHelperImpl() {
    }

    public static Component asNative(net.kyori.adventure.text.Component adventure) {
        return MinecraftClientAudiences.of().asNative(adventure);
    }
}
