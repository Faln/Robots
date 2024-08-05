package org.evokedev.evokerobots.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.menu.RobotMenu;
import org.evokedev.evokerobots.utils.RobotUtils;
import org.stormdev.abstracts.CommonListener;
import org.stormdev.scheduler.Scheduler;
import org.stormdev.utils.NBTEditor;

public final class RobotListener extends CommonListener<EvokeRobots> {

    public RobotListener(final EvokeRobots plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDisband(final IslandDisbandEvent event) {
        final Island island = event.getIsland();

        Scheduler.async().run(() -> {
            this.plugin.getRobotStorage().allValues().removeIf(robot -> {
                return robot.getOwner().equals(island.getUniqueId());
            });
        });
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.AIR || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack item = event.getItemInHand();

        if (!RobotUtils.isRobot(item)) {
            return;
        }

        event.setCancelled(true);

        final Player player = event.getPlayer();
        final Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.island-needed");
            return;
        }

        this.plugin.getRobotManager().initRobot(player, item, event.getBlock().getLocation().clone());

        item.setAmount(item.getAmount() - 1);
    }
}
