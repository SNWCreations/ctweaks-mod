package snw.mods.ctweaks.mod.client.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.protocol.Packet;
import snw.lib.protocol.serial.DeserializedPacket;
import snw.lib.protocol.serial.PacketDeserializer;
import snw.lib.protocol.serial.std.StandardPacketDeserializer;
import snw.mods.ctweaks.protocol.PacketTypes;
import snw.mods.ctweaks.protocol.handler.ClientboundPacketHandler;

@Slf4j
public class ModC2SConnection {
    private final PacketDeserializer<ClientboundPacketHandler> packetDeserializer;
    private final ClientboundPacketHandlerImpl packetHandler;

    public ModC2SConnection() {
        this.packetDeserializer = new StandardPacketDeserializer<>(PacketTypes.SERVERSIDE);
        this.packetHandler = new ClientboundPacketHandlerImpl();
    }

    public static ModC2SConnection getModC2SConnection() {
        final ClientPacketListener vanillaConnection = Minecraft.getInstance().getConnection();
        if (vanillaConnection != null) {
            return ((Getter) vanillaConnection).getCTweaksModConnection();
        } else {
            throw new IllegalStateException("Client is not connected to any server");
        }
    }

    public void onConnected() {
        log.info("Client connected to a server");
    }

    public void onDisconnect() {
        log.info("Client disconnecting from the server");
        this.packetHandler.close();
    }

    public void handlePayload(ModPayload payload) {
        try {
            final byte[] data = payload.data();
            final ByteArrayDataInput in = ByteStreams.newDataInput(data);
            final DeserializedPacket<ClientboundPacketHandler> packetContainer = packetDeserializer.deserialize(in);
            final snw.lib.protocol.packet.Packet<ClientboundPacketHandler> modPacket = packetContainer.packet();
            if (modPacket != null) {
                try {
                    modPacket.handle(packetHandler);
                } catch (Exception e) {
                    log.error("Failed to handle mod packet {}", modPacket, e);
                }
            } else {
                log.warn("Unrecognized mod packet {}", packetContainer.type());
            }
        } catch (Exception e) {
            log.error("Failed to handle payload", e);
        }
    }

    public interface Getter {
        ModC2SConnection getCTweaksModConnection();
    }
}
