package snw.mods.ctweaks.mod.client.render.layout;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import snw.mods.ctweaks.mod.client.render.ClientObject;
import snw.mods.ctweaks.mod.client.render.ClientObjectContainer;
import snw.mods.ctweaks.mod.client.render.ClientRenderable;
import snw.mods.ctweaks.object.IntKeyed;
import snw.mods.ctweaks.render.layout.Layout;

import java.util.List;
import java.util.function.Consumer;

public abstract class ClientLayout<T extends ClientRenderable> implements ClientObject, ClientObjectContainer {
    @Getter
    protected final int id;
    protected @Nullable List<T> children;

    protected ClientLayout(int id) {
        this.id = id;
    }

    public void visitChildren(Consumer<T> visitor) {
        if (children != null) {
            children.forEach(visitor);
        }
    }

    public abstract void arrangeElements();

    @Override
    public Key getType() {
        return Layout.TYPE;
    }

    @Override
    public boolean removeIfInside(IntKeyed.Descriptor otherObjectDesc) {
        boolean changed;
        if (children != null) {
            changed = children.removeIf(it -> it.describe().equals(otherObjectDesc));
            if (changed) {
                arrangeElements();
            }
        } else {
            changed = false;
        }
        return changed;
    }
}
