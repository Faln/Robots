package org.evokedev.evokerobots.price.provider;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface PriceProvider {

    double getSellPrice(final ItemStack item);

    double getSellPrice(final Material material, final int amount);

}
