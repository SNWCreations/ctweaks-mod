package snw.mods.ctweaks.mod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import snw.mods.ctweaks.object.pos.PlanePosition;

import java.util.UUID;

public class ClientPlayerFaceRenderer implements ClientRenderer {
    private final int id;
    private final UUID targetId;
    private PlanePosition position;
    private int size = 24;
    private ResourceLocation cachedSkinLocation;

    public ClientPlayerFaceRenderer(int id, UUID targetId) {
        this.id = id;
        this.targetId = targetId;
    }

    @Override
    public void render(GuiGraphics helper) {
        if (position == null) {
            return;
        }
        Minecraft minecraft = helper.getMinecraft();
        PlayerInfo targetInfo = minecraft.getConnection().getPlayerInfo(targetId);
        if (targetInfo != null) {
            ResourceLocation resourceLocation = targetInfo.getSkin().texture();
            if (!resourceLocation.equals(cachedSkinLocation)) {
                cachedSkinLocation = resourceLocation;
            }
        }
        helper.drawPlayerFace(cachedSkinLocation, position.x(), position.y(), size);
    }

    public void update() {
        // todo use somewhat packet
    }
}
