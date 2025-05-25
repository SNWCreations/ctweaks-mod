package snw.mods.ctweaks.mod.client.render;

import snw.mods.ctweaks.mod.client.object.PlanePositioned;
import snw.mods.ctweaks.mod.client.object.PlaneSized;
import snw.mods.ctweaks.object.pos.PlanePosition;

public interface ClientPlaneRenderable extends ClientRenderer, PlaneSized, PlanePositioned {
    void setPosition(PlanePosition position, boolean noPacket);
}
