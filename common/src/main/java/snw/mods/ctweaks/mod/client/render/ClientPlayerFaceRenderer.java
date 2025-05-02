package snw.mods.ctweaks.mod.client.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import snw.mods.ctweaks.object.pos.PlanePosition;
import snw.mods.ctweaks.protocol.packet.s2c.ClientboundUpdatePlayerFaceRendererPacket;

import java.util.Optional;
import java.util.UUID;

public class ClientPlayerFaceRenderer implements ClientRenderer {
    private final int id;
    private UUID targetId;
    private boolean requesting;
    private PlanePosition position;
    private int size = 24;
    private ResourceLocation cachedSkinLocation;

    public ClientPlayerFaceRenderer(int id) {
        this.id = id;
    }

    @Override
    public void render(GuiGraphics helper) {
        if (targetId == null || position == null) {
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
        if (cachedSkinLocation == null) {
            if (!requesting) {
                final UUID toRequest = targetId;
                GameProfile profile = new GameProfile(toRequest, "");
                minecraft.getSkinManager().getOrLoad(profile)
                        .thenAccept(loaded -> {
                            if (targetId.equals(toRequest)) {
                                cachedSkinLocation = loaded.texture();
                            } // or target changed so this texture should be dropped
                        })
                        .thenRun(() -> requesting = false);
                requesting = true;
            }
            // If server adds a player while the client have no information about it,
            //  we'll use default skin as fallback. The correct skin will be used when
            //  the info of the target player is received on the client through
            //  player info update packet or client loaded the textures.
            cachedSkinLocation = DefaultPlayerSkin.getDefaultTexture();
        }
        helper.drawPlayerFace(cachedSkinLocation, position.x(), position.y(), size);
    }

    public void update(ClientboundUpdatePlayerFaceRendererPacket packet) {
        UUID newTarget = Optional.ofNullable(packet.getTarget()).orElse(this.targetId);
        if (!newTarget.equals(this.targetId)) {
            this.requesting = false;
            this.cachedSkinLocation = null;
        }
        this.targetId = newTarget;
        this.position = Optional.ofNullable(packet.getPosition()).orElse(this.position);
        this.size = Optional.ofNullable(packet.getSize()).orElse(this.size);
    }
}
