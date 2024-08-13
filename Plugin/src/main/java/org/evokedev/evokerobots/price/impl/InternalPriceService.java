package org.evokedev.evokerobots.price.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evokedev.api.PriceService;
import org.evokedev.evokerobots.EvokeRobots;

public final class InternalPriceService implements PriceService {

    private final EvokeRobots plugin = EvokeRobots.get();

    @Override
    public double getSellPrice(final ItemStack item) {
        return this.plugin.getInternalPriceRegistry().get(item.getType()).orElse(0.0) * item.getAmount();
    }

    @Override
    public double getSellPrice(final Material material, final int amount) {
        return this.plugin.getInternalPriceRegistry().get(material).orElse(0.0) * amount;
    }
}
