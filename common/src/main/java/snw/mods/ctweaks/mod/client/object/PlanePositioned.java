package snw.mods.ctweaks.mod.client.object;

import snw.mods.ctweaks.object.pos.PlanePosition;

public interface PlanePositioned extends PlanePosition.Getter {
    void setPosition(PlanePosition position);
}
