package org.evokedev.evokerobots.price.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.utils.EclipseRegistry;
import org.stormdev.files.CommonConfig;
import org.stormdev.hooks.economy.provider.Economy;
import org.stormdev.hooks.economy.registry.impl.DefaultEconomyRegistry;

@RequiredArgsConstructor
@Getter
public final class InternalPriceRegistry extends EclipseRegistry<Material, Double> {

    private final EvokeRobots plugin;
    private Economy economy;

    public void load() {
        final CommonConfig config = this.plugin.getConfig("settings");

        this.economy = DefaultEconomyRegistry.get().getEconomy(config.getString("economy"));

        for (final String key : config.getSectionKeys("prices")) {
            this.register(Material.getMaterial(key.toUpperCase()), config.getDouble("prices." + key));
        }
    }
}
