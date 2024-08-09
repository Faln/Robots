package org.evokedev.evokerobots.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.manager.RobotManager;
import org.evokedev.evokerobots.tiers.Tier;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.evokedev.evokerobots.upgrade.impl.SpeedRobotUprade;
import org.evokedev.evokerobots.upgrade.impl.StorageRobotUpgrade;
import org.evokedev.evokerobots.utils.FormatUtils;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.hooks.economy.registry.impl.DefaultEconomyRegistry;
import org.stormdev.menus.v2.MenuBuilder;
import org.stormdev.menus.v2.templates.GenericCommonMenu;

public final class RobotMenu extends GenericCommonMenu<EvokeRobots> {

    private final RobotManager robotManager;

    public RobotMenu(final EvokeRobots plugin) {
        super(plugin, plugin.getConfig("menus"), "robot.");
        this.robotManager = plugin.getRobotManager();
    }

    public void open(final Player player, final Robot robot) {
        final MenuBuilder builder = this.createBase();
        final double sellPrice = robot.getSellPrice();
        final Tier tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());
        final SpeedRobotUprade speedUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(SpeedRobotUprade.class);
        final StorageRobotUpgrade storageUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .add("%radius%", String.valueOf(tier.getRadius()))
                .add("%speed%", String.valueOf(tier.getSpeed()))
                .add("%storage%", FormatUtils.formatComma(tier.getStorage()))
                .add("%speed-after-upgrade%", String.valueOf(tier.getSpeed() / speedUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED)).getValue()))
                .add("%storage-after-upgrade%", String.valueOf(tier.getStorage() * storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE)).getValue()))
                .add("%sell-price%", FormatUtils.formatComma(sellPrice))
                .add("%item-count%", FormatUtils.formatComma(robot.getItemCount()));

        builder.setItem(this.robotManager.getPickupMenuItem().getSlot(), this.robotManager.getPickupMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getPickupMenuItem().getSlot(), event -> {
            player.getInventory().addItem(this.robotManager.toItem(robot));
            this.robotManager.dispose(robot);

            player.closeInventory();
        });

        builder.setItem(this.robotManager.getUpgradesMenuItem().getSlot(), this.robotManager.getUpgradesMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getUpgradesMenuItem().getSlot(), event -> {
            new RobotUpgradeMenu(this.plugin).open(player, robot);
        });

        builder.setItem(this.robotManager.getSellMenuItem().getSlot(), this.robotManager.getSellMenuItem().getItem().parse(replacer));
        builder.addClickEvent(this.robotManager.getSellMenuItem().getSlot(), event -> {
            if (sellPrice <= 0) {
                return;
            }

            robot.clearContents();

            DefaultEconomyRegistry.get().getEconomy("vault").addBalance(player, sellPrice);

            this.plugin.getMessageCache().sendMessage(player, "messages.robot-sold", replacer);
            this.open(player, robot);
        });

        player.openInventory(builder.build());
    }

}
