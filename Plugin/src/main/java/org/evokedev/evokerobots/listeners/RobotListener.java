package org.evokedev.evokerobots.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.menu.RobotMenu;
import org.evokedev.evokerobots.utils.RobotUtils;
import org.stormdev.abstracts.CommonListener;

public final class RobotListener extends CommonListener<EvokeRobots> {

    public RobotListener(final EvokeRobots plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlace(final PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType() == Material.AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack item = event.getItem();

        if (!RobotUtils.isRobot(item) || event.getClickedBlock() == null) {
            return;
        }

        event.setCancelled(true);
        item.setAmount(item.getAmount() - 1);

        this.plugin.getRobotManager().initRobot(event.getPlayer(), item, event.getClickedBlock().getLocation());
    }

    @EventHandler
    public void onClick(final PlayerInteractAtEntityEvent event) {
        final Location location = event.getRightClicked().getLocation().clone();

        if (!this.plugin.getRobotStorage().contains(location)) {
            if (!this.plugin.getRobotStorage().contains(location.clone().add(0, 1, 0))) {
                return;
            }

            location.add(0, 1, 0);
        }

        final Robot robot = this.plugin.getRobotStorage().get(location);

        new RobotMenu(this.plugin).open(event.getPlayer(), robot);
    }
}
