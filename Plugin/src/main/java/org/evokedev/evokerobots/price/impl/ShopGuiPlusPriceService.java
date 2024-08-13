package org.evokedev.evokerobots.price.impl;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.evokedev.api.PriceService;

public final class ShopGuiPlusPriceService implements PriceService {

    @Override
    public double getSellPrice(final ItemStack item) {
        return ShopGuiPlusApi.getItemStackPriceSell(item);
    }

    @Override
    public double getSellPrice(final Material material, final int amount) {
        return ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(material, amount));
    }
}
