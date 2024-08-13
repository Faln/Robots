package org.evokedev.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface PriceService {

    double getSellPrice(final ItemStack item);

    double getSellPrice(final Material material, final int amount);

}
