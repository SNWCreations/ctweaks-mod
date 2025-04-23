package snw.mods.ctweaks.mod.util;

import java.util.function.Consumer;

public final class Util {
    private Util() {
    }

    public static <T> void apply(T obj, Consumer<T> action) {
        action.accept(obj);
    }
}
