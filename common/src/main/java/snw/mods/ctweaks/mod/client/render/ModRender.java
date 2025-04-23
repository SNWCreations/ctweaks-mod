package snw.mods.ctweaks.mod.client.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.kyori.adventure.key.Key;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import snw.mods.ctweaks.render.TextRenderer;

import java.util.HashMap;
import java.util.Map;

import static snw.mods.ctweaks.mod.util.Util.apply;

@Slf4j
public class ModRender {
    private final Map<Key, Int2ObjectFunction<ClientRenderer>> rendererFactories = new HashMap<>();
    private final Int2ObjectMap<ClientRenderer> renderers = new Int2ObjectLinkedOpenHashMap<>();

    {
        apply(rendererFactories, it -> {
            it.put(TextRenderer.TYPE, ClientTextRenderer::new);
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

    public boolean remove(int id) {
        return renderers.remove(id) != null;
    }

    public void clear() {
        renderers.clear();
    }

    public @Nullable ClientRenderer getRenderer(int id) {
        return renderers.get(id);
    }

    public ClientRenderer addRenderer(int id, Key type) {
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
}
