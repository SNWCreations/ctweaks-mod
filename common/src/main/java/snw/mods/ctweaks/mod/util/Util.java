package snw.mods.ctweaks.mod.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Util {
    private Util() {
    }

    public static <T> void apply(T obj, Consumer<T> action) {
        action.accept(obj);
    }

    public static <T, R extends T> Function<T, @Nullable R> caster(Class<R> type) {
        return t -> type.isInstance(t) ? type.cast(t) : null;
    }
}
