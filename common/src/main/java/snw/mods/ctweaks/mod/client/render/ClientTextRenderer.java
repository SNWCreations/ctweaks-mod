package snw.mods.ctweaks.mod.client.render;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import snw.mods.ctweaks.mod.util.AdventureHelper;
import snw.mods.ctweaks.object.pos.PlanePosition;
import snw.mods.ctweaks.protocol.packet.s2c.ClientboundUpdateTextRendererPacket;

import java.util.Objects;
import java.util.Optional;

public class ClientTextRenderer implements ClientRenderer {
    private final int id;
    private PlanePosition pos;
    private Component text;
    private float scale = 1.0F;
    private boolean noShadow;

    public ClientTextRenderer(int id) {
        this.id = id;
    }

    public void update(ClientboundUpdateTextRendererPacket packet) {
        this.pos = Objects.requireNonNullElse(packet.getNewPosition(), this.pos);
        this.text = Optional.ofNullable(packet.getText()).map(AdventureHelper::asNative).orElse(this.text);
        this.scale = Optional.ofNullable(packet.getScale()).orElse(this.scale);
        this.noShadow = Optional.ofNullable(packet.getNoShadow()).orElse(this.noShadow);
    }

    @Override
    public void render(GuiGraphics helper) {
        if (this.pos != null && this.text != null) {
            final Font gameFont = helper.getMinecraft().font;
            helper.drawString(gameFont, this.text, this.pos.x(), this.pos.y(), -1, this.noShadow, this.scale);
        }
    }
}
