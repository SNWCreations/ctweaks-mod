package snw.mods.ctweaks.mod.client.render;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kyori.adventure.key.Key;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import snw.mods.ctweaks.mod.client.render.layout.ClientGridLayout;
import snw.mods.ctweaks.mod.client.render.layout.ClientLayout;
import snw.mods.ctweaks.object.IntKeyed;
import snw.mods.ctweaks.render.PlayerFaceRenderer;
import snw.mods.ctweaks.render.Renderer;
import snw.mods.ctweaks.render.TextRenderer;
import snw.mods.ctweaks.render.layout.GridLayout;
import snw.mods.ctweaks.render.layout.Layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static snw.mods.ctweaks.mod.util.Util.apply;

@Slf4j
public class ModRender {
    private final Map<Key, Int2ObjectFunction<ClientRenderer>> rendererFactories = new HashMap<>();
    private final Map<Key, Int2ObjectFunction<ClientLayout<?>>> layoutFactories = new HashMap<>();
    private final Int2ObjectMap<ClientRenderer> renderers = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectMap<ClientLayout<?>> layouts = new Int2ObjectLinkedOpenHashMap<>();

    {
        apply(rendererFactories, it -> {
            it.put(TextRenderer.EXACT_TYPE, ClientTextRenderer::new);
            it.put(PlayerFaceRenderer.EXACT_TYPE, ClientPlayerFaceRenderer::new);
        });
        apply(layoutFactories, it -> {
            it.put(GridLayout.EXACT_TYPE, ClientGridLayout::new);
            // todo implement linear layout later
        });
    }

    public interface Getter {
        ModRender getCTweaksModRender();
    }

    public static ModRender getModRender() {
        return ((Getter) Minecraft.getInstance().gui).getCTweaksModRender();
    }

    public void render(GuiGraphics helper) {
        for (Int2ObjectMap.Entry<ClientRenderer> entry : renderers.int2ObjectEntrySet()) {
            final ClientRenderer renderer = entry.getValue();
            renderer.render(helper);
        }
    }

    public boolean removeRenderer(int id) {
        ClientRenderable removed = renderers.remove(id);
        boolean result = removed != null;
        if (result) {
            IntKeyed.Descriptor descriptor = removed.describe();
            removeFromObjectContainers(descriptor);
        }
        return result;
    }

    public boolean removeLayout(int id) {
        ClientLayout<?> removed = layouts.remove(id);
        boolean result = removed != null;
        if (result) {
            IntKeyed.Descriptor descriptor = removed.describe();
            removeFromObjectContainers(descriptor);
        }
        return result;
    }

    private void removeFromObjectContainers(IntKeyed.Descriptor descriptor) {
        Stream<? extends ClientObjectContainer> objectContainers;
        objectContainers = layouts.values().stream();
        objectContainers.forEach(it -> it.removeIfInside(descriptor));
    }

    public void clearRenderers() {
        renderers.clear();
    }

    public void clearLayouts() {
        layouts.clear();
    }

    public @Nullable ClientRenderable getRenderer(int id) {
        return renderers.get(id);
    }

    public @Nullable ClientLayout<?> getLayout(int id) {
        return layouts.get(id);
    }

    public ClientRenderer addRenderer(IntKeyed.Descriptor descriptor) {
        int id = descriptor.id();
        Key type = descriptor.exactType();
        if (renderers.containsKey(id)) {
            throw new IllegalStateException("Renderer with id " + id + " already exists");
        }
        val factory = rendererFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown renderer type: " + type);
        }
        final ClientRenderer created = factory.apply(id);
        log.info("Created renderer with ID {} and type {}", id, type);
        renderers.put(id, created);
        return created;
    }

    public ClientLayout<?> addLayout(IntKeyed.Descriptor descriptor) {
        int id = descriptor.id();
        Key type = descriptor.exactType();
        val factory = layoutFactories.get(type);
        Preconditions.checkArgument(factory != null, "Unknown layout type %s", type);
        val created = factory.apply(id);
        log.info("Created layout with ID {} and type {}", id, type);
        layouts.put(id, created);
        return created;
    }

    @Contract("null, _ -> null; !null, _ -> !null")
    public <T extends ClientObject> List<T> lookupClientObject(@Nullable List<IntKeyed.Descriptor> descriptors, Function<ClientObject, @Nullable T> converter) {
        return descriptors == null ? null :
                descriptors.stream()
                        .map(this::lookupClientObject)
                        .map(converter)
                        .filter(Objects::nonNull)
                        .toList();
    }

    public ClientObject lookupClientObject(IntKeyed.Descriptor descriptor) {
        Key type = descriptor.type();
        Int2ObjectMap<? extends ClientObject> registry;
        if (type.equals(Renderer.TYPE)) {
            registry = renderers;
        } else if (type.equals(Layout.TYPE)) {
            registry = layouts;
        } else {
            throw new IllegalArgumentException("Unknown object type " + type);
        }
        int id = descriptor.id();
        return registry.get(id);
    }
}
