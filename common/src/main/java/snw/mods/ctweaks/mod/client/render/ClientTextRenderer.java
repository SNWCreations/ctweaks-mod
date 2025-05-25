package snw.mods.ctweaks.mod.client.render;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.kyori.adventure.key.Key;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import snw.mods.ctweaks.mod.util.AdventureHelper;
import snw.mods.ctweaks.object.pos.PlanePosition;
import snw.mods.ctweaks.protocol.packet.c2s.ServerboundSetObjectPlanePosPacket;
import snw.mods.ctweaks.protocol.packet.s2c.ClientboundUpdateTextRendererPacket;
import snw.mods.ctweaks.render.Renderer;
import snw.mods.ctweaks.render.TextRenderer;

import java.util.Objects;
import java.util.Optional;

import static snw.lib.protocol.util.PacketHelper.newNonce;
import static snw.mods.ctweaks.ModConstants.UNIT_AS_INT;
import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;
import static snw.mods.ctweaks.object.pos.PlanePosition.planePos;

public class ClientTextRenderer implements ClientPlaneRenderable {
    @Getter
    private final int id;
    @Getter
    private PlanePosition position;
    private Component text;
    private float scale = 1.0F;
    private boolean noShadow;
    private int outlineColor = UNIT_AS_INT;

    public ClientTextRenderer(int id) {
        this.id = id;
    }

    public void update(ClientboundUpdateTextRendererPacket packet) {
        this.noShadow = Optional.ofNullable(packet.getNoShadow()).orElse(this.noShadow);
        this.outlineColor = Objects.requireNonNullElse(packet.getOutlineColor(), this.outlineColor);
        if (packet.getNewPosition() != null) {
            this.text = Optional.ofNullable(packet.getText()).map(AdventureHelper::asNative).orElse(this.text);
            this.scale = Optional.ofNullable(packet.getScale()).orElse(this.scale);
        } else {
            val oldWidth = this.getWidth();
            val oldHeight = this.getHeight();
            this.text = Optional.ofNullable(packet.getText()).map(AdventureHelper::asNative).orElse(this.text);
            this.scale = Optional.ofNullable(packet.getScale()).orElse(this.scale);
            val widthDiff = (oldWidth - this.getWidth()) / 2;
            val heightDiff = (oldHeight - this.getHeight()) / 2;
            this.setPosition(planePos(this.position.x() + widthDiff, this.position.y() + heightDiff));
        }
    }

    @Override
    public void render(GuiGraphics helper) {
        if (this.position != null && this.text != null) {
            final Font gameFont = helper.getMinecraft().font;
            if (outlineColor != UNIT_AS_INT) {
                helper.drawStringWithOutline(gameFont, this.text, this.position.x(), this.position.y(), -1, this.outlineColor, this.scale);
            } else {
                helper.drawString(gameFont, this.text, this.position.x(), this.position.y(), -1, this.noShadow, this.scale);
            }
        }
    }

    @Override
    public int getWidth() {
        if (this.text != null) {
            Font gameFont = Minecraft.getInstance().font;
            return Math.round(gameFont.width(this.text) * this.scale);
        }
        return 0;
    }

    @Override
    public int getHeight() {
        if (this.text != null) {
            Font gameFont = Minecraft.getInstance().font;
            return Math.round(gameFont.wordWrapHeight(this.text, getWidth()) * this.scale);
        }
        return 0;
    }

    @Override
    public void setPosition(@NonNull PlanePosition position) {
        this.position = position;
        getModC2SConnection().sendModPacket(() -> new ServerboundSetObjectPlanePosPacket(describe(), this.position, newNonce()));
    }

    @Override
    public Key getType() {
        return Renderer.TYPE;
    }

    @Override
    public Key getExactType() {
        return TextRenderer.EXACT_TYPE;
    }
}
