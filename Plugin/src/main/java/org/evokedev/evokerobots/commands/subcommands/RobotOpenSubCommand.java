package org.evokedev.evokerobots.commands.subcommands;

import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.menu.RobotMenu;
import org.evokedev.evokerobots.npc.NPC;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class RobotOpenSubCommand extends CommonSubCommand<EvokeRobots> {

    public RobotOpenSubCommand(final EvokeRobots plugin) {
        super(plugin, 1, Sets.immutable.of("open"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final Player player = context.getSender();

        if (!player.hasPermission("evokerobots.admin")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission");
            return;
        }

        final int npcId = context.asInt(0);

        new RobotMenu(this.plugin).open(player, this.plugin.getRobotStorage().get(NPC.getNpcProvider().getLocation(npcId)));
    }
}
