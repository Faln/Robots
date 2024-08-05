package org.evokedev.evokerobots.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import io.github.gonalez.znpcs.npc.event.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.menu.RobotMenu;
import org.stormdev.abstracts.CommonListener;

public final class ZNPCRobotListener extends CommonListener<EvokeRobots> {

    public ZNPCRobotListener(final EvokeRobots plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(final NPCInteractEvent event) {
        final Location location = event.getNpc().getLocation().clone().add(-0.5, 0, -0.5);

        if (!this.plugin.getRobotStorage().contains(location)) {
            if (!this.plugin.getRobotStorage().contains(location.clone().add(0, 1, 0))) {
                return;
            }

            location.add(0, 1, 0);
        }

        final Player player = event.getPlayer();
        final Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island.getIslandMembers(true).stream().map(SuperiorPlayer::getUniqueId).anyMatch(id -> !id.equals(player.getUniqueId()))) {
            return;
        }

        final Robot robot = this.plugin.getRobotStorage().get(location);

        new RobotMenu(this.plugin).open(event.getPlayer(), robot);
    }
}
