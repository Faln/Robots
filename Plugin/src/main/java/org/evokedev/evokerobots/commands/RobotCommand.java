package org.evokedev.evokerobots.commands;

import org.bukkit.command.CommandSender;
import org.evokedev.evokerobots.EvokeRobots;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

public final class RobotCommand extends CommonCommand<EvokeRobots, CommandSender> {


    public RobotCommand(final EvokeRobots plugin) {
        super(plugin, "robot", CommandSender.class);
    }

    @Override
    public void execute(final CommandContext<CommandSender> context) {
        final CommandSender player = context.getSender();

        if (!player.hasPermission("evokerobots.admin")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission");
            return;
        }

        this.plugin.getMessageCache().sendMessage(player, "messages.help");
    }
}
