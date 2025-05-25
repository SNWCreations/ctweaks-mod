package snw.mods.ctweaks.mod.client.render.layout;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import snw.mods.ctweaks.mod.client.object.PlanePositioned;
import snw.mods.ctweaks.mod.client.object.PlaneSized;
import snw.mods.ctweaks.mod.client.render.ClientPlaneRenderable;
import snw.mods.ctweaks.object.IntKeyed;
import snw.mods.ctweaks.object.pos.PlanePosition;
import snw.mods.ctweaks.object.range.Rectangle;
import snw.mods.ctweaks.protocol.packet.c2s.ServerboundSetObjectPlanePosPacket;
import snw.mods.ctweaks.protocol.packet.s2c.ClientboundUpdateGridLayoutPacket;
import snw.mods.ctweaks.render.layout.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;
import static snw.lib.protocol.util.PacketHelper.newNonce;
import static snw.mods.ctweaks.mod.client.net.ModC2SConnection.getModC2SConnection;
import static snw.mods.ctweaks.mod.client.render.ModRender.getModRender;
import static snw.mods.ctweaks.mod.util.Util.caster;
import static snw.mods.ctweaks.object.pos.PlanePosition.planePos;

public class ClientGridLayout extends ClientLayout<ClientPlaneRenderable> implements PlanePositioned, PlaneSized {
    @Getter
    private PlanePosition position;
    private @Nullable Rectangle range;
    private int rowSpacing;
    private int columnSpacing;
    // todo we may rename the following fields in further versions
    private int rowCount = 1; // = column size
    private int columnCount = 1; // = row size
    @Getter
    private int width;
    @Getter
    private int height;

    public ClientGridLayout(int id) {
        super(id);
    }

    public void update(ClientboundUpdateGridLayoutPacket packet) {
        List<Descriptor> newElementsDesc = packet.getNewElements();
        if (newElementsDesc != null) {
            this.children = getModRender().lookupClientObject(newElementsDesc, caster(ClientPlaneRenderable.class));
        }
        this.position = requireNonNullElse(packet.getNewPosition(), this.position);
        Optional<Rectangle> newRange = packet.getNewRange();
        // noinspection OptionalAssignedToNull
        if (newRange != null) {
            this.range = newRange.orElse(null);
        }
        this.rowSpacing = requireNonNullElse(packet.getRowSpacing(), this.rowSpacing);
        this.columnSpacing = requireNonNullElse(packet.getColumnSpacing(), this.columnSpacing);
        this.rowCount = requireNonNullElse(packet.getRowCount(), this.rowCount);
        this.columnCount = requireNonNullElse(packet.getColumnCount(), this.columnCount);

        arrangeElements();
    }

    @Override
    public void arrangeElements() {
        if (position != null && children != null && !children.isEmpty()) {
            List<List<ClientPlaneRenderable>> lines = Lists.partition(children, columnCount);
            int elementMaxWidth = lines.stream()
                    .flatMapToInt(it -> it.stream()
                            .mapToInt(PlaneSized::getWidth))
                    .max().orElseThrow();
            int elementMaxHeight = lines.stream()
                    .flatMapToInt(it -> it.stream()
                            .mapToInt(PlaneSized::getHeight))
                    .max().orElseThrow();
            int totalWidthNoSpacing = elementMaxWidth * columnCount;
            int totalHeightNoSpacing = elementMaxHeight * rowCount;
            int totalWidth = totalWidthNoSpacing + (columnCount - 1) * columnSpacing;
            int totalHeight = totalHeightNoSpacing + (rowCount - 1) * rowSpacing;
            int startX, startY, finalRowSpacing, finalColumnSpacing;
            if (range != null) {
                int widthDiff = range.width() - totalWidth;
                int heightDiff = range.height() - totalHeight;
                if (widthDiff >= 0) {
                    startX = position.x() + (widthDiff / 2);
                    finalColumnSpacing = columnSpacing;

                    width = range.width();
                } else {
                    startX = position.x();
                    int widthDiffNoSpacing = range.width() - totalWidthNoSpacing;
                    if (widthDiffNoSpacing >= 0) {
                        finalColumnSpacing = columnCount == 1 ? 0 : (widthDiffNoSpacing / (columnCount - 1));

                        width = range.width();
                    } else {
                        finalColumnSpacing = columnSpacing;

                        width = totalWidth;
                    }
                }
                if (heightDiff >= 0) {
                    startY = position.y() + (heightDiff / 2);
                    finalRowSpacing = rowSpacing;

                    height = range.height();
                } else {
                    startY = position.y();
                    int heightDiffNoSpacing = range.height() - totalHeightNoSpacing;
                    if (heightDiffNoSpacing >= 0) {
                        finalRowSpacing = rowCount == 1 ? 0 : (heightDiffNoSpacing / (rowCount - 1));

                        height = range.height();
                    } else {
                        finalRowSpacing = rowSpacing;

                        height = totalHeight;
                    }
                }
            } else {
                startX = position.x();
                startY = position.y();
                finalRowSpacing = rowSpacing;
                finalColumnSpacing = columnSpacing;

                width = totalWidth;
                height = totalHeight;
            }
            List<Pair<IntKeyed.Descriptor, PlanePosition>> updatedPositions = new ArrayList<>();
            int i = 0; // processed lines in this call
            for (List<ClientPlaneRenderable> line : lines) {
                int j = 0; // processed items in this line
                int lineY = startY + (i * (elementMaxHeight + finalRowSpacing));
                for (ClientPlaneRenderable renderable : line) {
                    int width = renderable.getWidth();
                    int xOffset = (elementMaxWidth - width) / 2;
                    int boxX = startX + (j * (elementMaxWidth + finalColumnSpacing));
                    int x = boxX + xOffset;
                    Descriptor descriptor = renderable.describe();
                    PlanePosition pos = planePos(x, lineY);
                    val pair = ObjectObjectImmutablePair.of(descriptor, pos);
                    updatedPositions.add(pair);
                    j++;
                }
                i++;
            }
            getModC2SConnection().sendModPacket(() -> new ServerboundSetObjectPlanePosPacket(updatedPositions, newNonce()));
        }
    }

    @Override
    public void setPosition(@NonNull PlanePosition position) {
        this.position = position;
        getModC2SConnection().sendModPacket(() -> new ServerboundSetObjectPlanePosPacket(describe(), position, newNonce()));
    }

    @Override
    public Key getExactType() {
        return GridLayout.EXACT_TYPE;
    }
}
