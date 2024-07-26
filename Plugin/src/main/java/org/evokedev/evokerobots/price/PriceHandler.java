package org.evokedev.evokerobots.price;

import org.bukkit.Bukkit;
import org.evokedev.evokerobots.price.provider.PriceProvider;
import org.evokedev.evokerobots.price.provider.impl.ShopGuiPlusPriceProvider;

public final class PriceHandler {

    private static PriceProvider priceHandler;

    private PriceHandler() {}

    public static PriceProvider getInstance() {
        if (priceHandler == null) {
            priceHandler = getHandler();
        }

        return priceHandler;
    }

    private static PriceProvider getHandler() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ShopGuiPlus")) {
            return new ShopGuiPlusPriceProvider();
        }

        throw new IllegalStateException("No price handler found");
    }
}
