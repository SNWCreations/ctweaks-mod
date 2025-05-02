package snw.mods.ctweaks.mod.client.net;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import snw.lib.protocol.packet.Packet;
import snw.mods.ctweaks.ModConstants;
import snw.mods.ctweaks.mod.client.ClientWindow;
import snw.mods.ctweaks.mod.client.render.ClientPlayerFaceRenderer;
import snw.mods.ctweaks.mod.client.render.ClientRenderer;
import snw.mods.ctweaks.mod.client.render.ClientTextRenderer;
import snw.mods.ctweaks.object.IntIdentified;
import snw.mods.ctweaks.protocol.handler.ClientboundPacketHandler;
import snw.mods.ctweaks.protocol.packet.c2s.ServerboundReadyPacket;
import snw.mods.ctweaks.protocol.packet.s2c.*;

import java.util.function.BiConsumer;

import static snw.lib.protocol.util.PacketHelper.newNonce;
import static snw.mods.ctweaks.mod.client.render.ModRender.getModRender;

@Slf4j
public class ClientboundPacketHandlerImpl implements ClientboundPacketHandler {
    private final ModC2SConnection parent;

    public ClientboundPacketHandlerImpl(ModC2SConnection parent) {
        this.parent = parent;
    }

    public void close() {
        clearRenderers();
    }

    private void clearRenderers() {
        getModRender().clear();
    }

    @Override
    public void handleAddRenderer(ClientboundAddRendererPacket packet) {
        getModRender().addRenderer(packet.getId(), packet.getRendererType());
    }

    @Override
    public void handleClearRenderer(ClientboundClearRendererPacket packet) {
        clearRenderers();
    }

    private DisconnectedScreen createDisconnectedScreen(Component message) {
        return new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()), Component.translatable("disconnect.lost"), message);
    }

    @Override
    public void handleHello(ClientboundHelloPacket packet) {
        final int serverProtocolVer = packet.getProtocolVersion();
        if (ModConstants.PROTOCOL_VERSION != serverProtocolVer) {
            log.error("Incompatible mod protocol version {} on server, disconnecting", serverProtocolVer);
            Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().disconnect(
                        createDisconnectedScreen(
                                Component.translatable("snwctweaks.handshake.fail.incompatible_protocol",
                                        serverProtocolVer, ModConstants.PROTOCOL_VERSION)
                        )
                );
            });
        } else {
            log.info("Connected to a server with CTweaks installed");
            parent.serverModInstalled = true;
            ((ClientWindow) Minecraft.getInstance()).sendWindowProperties();
            parent.sendModPacket(() -> new ServerboundReadyPacket(newNonce()));
        }
    }

    @Override
    public void handleRemoveRenderer(ClientboundRemoveRendererPacket packet) {
        final int id = packet.getId();
        final boolean removed = getModRender().remove(id);
        if (!removed) {
            log.warn("Attempted to remove unknown renderer with ID {}", id);
        } else {
            log.info("Removed renderer with ID {}", id);
        }
    }

    @Override
    public void handleUpdatePlayerFaceRenderer(ClientboundUpdatePlayerFaceRendererPacket packet) {
        updateRenderer(packet, ClientPlayerFaceRenderer.class, ClientPlayerFaceRenderer::update);
    }

    @Override
    public void handleUpdateTextRenderer(ClientboundUpdateTextRendererPacket packet) {
        updateRenderer(packet, ClientTextRenderer.class, ClientTextRenderer::update);
    }

    private <R extends ClientRenderer, P extends Packet<ClientboundPacketHandler> & IntIdentified>
    void updateRenderer(P packet, Class<R> type, BiConsumer<R, P> updater) {
        final int id = packet.getId();
        final ClientRenderer result = getModRender().getRenderer(id);
        if (type.isInstance(result)) {
            final R casted = type.cast(result);
            updater.accept(casted, packet);
        } else if (result != null) {
            log.error("Attempted to update renderer with ID {} but has mismatched type, expected {} but got {}", id, type, result.getClass());
        } else {
            log.error("Attempted to update unknown renderer with ID {}", id);
        }
    }

}
