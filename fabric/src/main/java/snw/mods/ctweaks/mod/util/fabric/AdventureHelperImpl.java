package snw.mods.ctweaks.mod.util.fabric;

import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.minecraft.network.chat.Component;

public final class AdventureHelperImpl {
    private AdventureHelperImpl() {
    }

    public static Component asNative(net.kyori.adventure.text.Component adventure) {
        return FabricClientAudiences.of().toNative(adventure);
    }
}
