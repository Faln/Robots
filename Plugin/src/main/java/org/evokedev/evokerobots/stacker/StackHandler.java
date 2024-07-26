package org.evokedev.evokerobots.stacker;

import org.bukkit.Bukkit;
import org.evokedev.evokerobots.stacker.provider.StackProvider;
import org.evokedev.evokerobots.stacker.provider.impl.EmptyStackerProvider;
import org.evokedev.evokerobots.stacker.provider.impl.RoseStackerProvider;

public final class StackHandler {

    private static StackProvider stackProvider;

    private StackHandler() {}

    public static StackProvider getStackProvider() {
        if (stackProvider == null) {
            stackProvider = get();
        }

        return stackProvider;
    }

    private static StackProvider get() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("RoseStacker")) {
            return new RoseStackerProvider();
        }

        return new EmptyStackerProvider();
    }
}
