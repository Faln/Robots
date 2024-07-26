package org.evokedev.evokerobots.menu;

import lombok.val;
import org.bukkit.entity.Player;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.upgrade.RobotUpgrade;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.evokedev.evokerobots.upgrade.impl.SpeedRobotUprade;
import org.evokedev.evokerobots.upgrade.impl.StorageRobotUpgrade;
import org.evokedev.evokerobots.utils.FormatUtils;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.files.CommonConfig;
import org.stormdev.hooks.economy.registry.impl.DefaultEconomyRegistry;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.item.MenuItemBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class RobotUpgradeMenu extends GenericCommonMenu<EvokeRobots> {

    private final CommonConfig config;

    public RobotUpgradeMenu(final EvokeRobots plugin) {
        super(plugin, plugin.getConfig("menus"), "robot-upgrades.");
        this.config = plugin.getConfig("menus");
    }

    public void open(final Player player, final Robot robot) {
        final MenuBuilder builder = this.createBase();
        final MenuItemBuilder speedItem = this.config.getMenuItemBuilder("robot-upgrades.speed");
        final MenuItemBuilder storageItem = this.config.getMenuItemBuilder("robot-upgrades.storage");

        val speedUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(SpeedRobotUprade.class);
        val speed = speedUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED));
        val storageUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);
        val storage = storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE));

        builder.setItem(speedItem.getSlot(), speedItem.getItem().parse(new PlaceholderReplacer()
                .addPlaceholder("%current%", String.valueOf(robot.getUpgrade(RobotUpgradeType.SPEED)))
                .addPlaceholder("%max%", String.valueOf(speedUpgrade.getLevels().values().stream()
                        .map(RobotUpgrade.UpgradeDTO::getValue)
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .getAsDouble()))
                .addPlaceholder("%speed%", String.valueOf(speed.getValue()))
                .addPlaceholder("%cost%", speed.getCost() == 0.0 ? "Maxed" : FormatUtils.formatComma(speed.getCost()))
        ));

        builder.addClickEvent(speedItem.getSlot(), event -> {
            if (speed.getLevel() == speedUpgrade.getLevels().keySet().stream().mapToInt(Integer::intValue).max().getAsInt()) {
                this.plugin.getMessageCache().sendMessage(player, "messages.upgrade-maxed");
                return;
            }

            if (!DefaultEconomyRegistry.get().getEconomy("vault").hasBalance(player, speed.getCost())) {
                this.plugin.getMessageCache().sendMessage(player, "messages.not-enough-money");
                return;
            }

            DefaultEconomyRegistry.get().getEconomy("vault").withdrawBalance(player, speed.getCost());
            robot.upgrade(RobotUpgradeType.SPEED);

            this.plugin.getMessageCache().sendMessage(player, "messages.speed-upgraded");
            this.open(player, robot);
        });

        builder.setItem(storageItem.getSlot(), storageItem.getItem().parse(new PlaceholderReplacer()
                .addPlaceholder("%current%", String.valueOf(robot.getUpgrade(RobotUpgradeType.STORAGE)))
                .addPlaceholder("%max%", String.valueOf(storageUpgrade.getLevels().values().stream()
                        .map(RobotUpgrade.UpgradeDTO::getValue)
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .getAsDouble()))
                .addPlaceholder("%storage%", FormatUtils.formatComma(storage.getValue()))
                .addPlaceholder("%cost%", speed.getCost() == 0.0 ? "Maxed" : FormatUtils.formatComma(storage.getCost()))
        ));

        builder.addClickEvent(storageItem.getSlot(), event -> {
            if (storage.getLevel() == storageUpgrade.getLevels().keySet().stream().mapToInt(Integer::intValue).max().getAsInt()) {
                this.plugin.getMessageCache().sendMessage(player, "messages.upgrade-maxed");
                return;
            }

            if (!DefaultEconomyRegistry.get().getEconomy("vault").hasBalance(player, storage.getCost())) {
                this.plugin.getMessageCache().sendMessage(player, "messages.not-enough-money");
                return;
            }

            DefaultEconomyRegistry.get().getEconomy("vault").withdrawBalance(player, storage.getCost());
            robot.upgrade(RobotUpgradeType.STORAGE);

            this.plugin.getMessageCache().sendMessage(player, "messages.storage-upgraded");
            this.open(player, robot);
        });

        player.openInventory(builder.build());
    }
}
