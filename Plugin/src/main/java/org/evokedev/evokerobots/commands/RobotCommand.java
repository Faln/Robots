package org.evokedev.evokerobots.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.evokedev.evokerobots.EvokeRobots;
import org.jetbrains.annotations.NotNull;
import org.stormdev.commands.CommonCommand;
import org.stormdev.commands.context.CommandContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class RobotCommand extends CommonCommand<EvokeRobots, CommandSender> {

    private static final List<String> SUBCOMMANDS = Arrays.asList("give");

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

    @Override @NotNull
    public List<String> tabComplete(
            @NotNull final CommandSender sender,
            @NotNull final String alias,
            @NotNull final String[] args
    ) {
        if (!sender.hasPermission("evokerobots.tabcomplete")) {
            return Collections.emptyList();
        }

        final String arg0 = args[0];

        switch (args.length) {
            case 1:
                return SUBCOMMANDS;
            case 2:
                switch (arg0) {
                    case "give":
                        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            case 3:
                switch (arg0) {
                    case "give":
                        return new ArrayList<>(this.plugin.getTierRegistry().keySet());
                    default:
                        return Collections.emptyList();
                }
            case 4:
                switch (arg0) {
                    case "give":
                        return Lists.immutable.of("1", "2", "3").castToList();
                    default:
                        return Collections.emptyList();
                }
        }

        return Collections.emptyList();
    }
}
