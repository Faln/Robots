package org.evokedev.evokerobots.tiers.registry;

import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.tiers.Tier;
import org.evokedev.evokerobots.type.RobotType;
import org.evokedev.evokerobots.utils.EclipseRegistry;
import org.stormdev.files.CommonConfig;

@AllArgsConstructor
public final class TierRegistry extends EclipseRegistry<String, Tier> {

    private final EvokeRobots plugin;

    public void load() {
        this.getRegistry().clear();

        final CommonConfig config = this.plugin.getConfig("robots");

        for (final String key : config.getSectionKeys("")) {
            for (final String s : config.getSectionKeys(key)) {
                final String path = key + "." + s;
                final String tier = key.toLowerCase() + "-" + s;

                this.register(tier, Tier.of(
                        tier,
                        RobotType.valueOf(key.toUpperCase()),
                        config.getColoredString(path + ".npc-name"),
                        config.getDouble(path + ".speed"),
                        config.getDouble(path + ".storage"),
                        config.getItemBuilder(path + ".item"),
                        this.readEquipment(config, path))
                );
            }
        }
    }

    private ItemStack[] readEquipment(final CommonConfig config, final String path) {
        final ItemStack[] itemstack = new ItemStack[5];

        itemstack[0] = config.getItemStack(path + ".helmet");
        itemstack[1] = config.getItemStack(path + ".chestplate");
        itemstack[2] = config.getItemStack(path + ".leggings");
        itemstack[3] = config.getItemStack(path + ".boots");
        itemstack[4] = config.getItemStack(path + ".main-hand");

        return itemstack;
    }
}
