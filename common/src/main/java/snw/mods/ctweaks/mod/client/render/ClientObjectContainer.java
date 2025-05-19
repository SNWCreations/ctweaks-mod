package snw.mods.ctweaks.mod.client.render;

import snw.mods.ctweaks.object.IntKeyed;

public interface ClientObjectContainer {
    boolean removeIfInside(IntKeyed.Descriptor otherObjectDesc);
}
