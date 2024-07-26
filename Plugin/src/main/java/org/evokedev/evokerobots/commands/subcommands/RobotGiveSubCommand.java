package org.evokedev.evokerobots.commands.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.impl.factory.Sets;
import org.evokedev.evokerobots.EvokeRobots;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.commands.CommonSubCommand;
import org.stormdev.commands.context.CommandContext;

public final class RobotGiveSubCommand extends CommonSubCommand<EvokeRobots> {


    public RobotGiveSubCommand(final EvokeRobots plugin) {
        super(plugin, 3, Sets.immutable.of("give"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final Player player = context.asPlayer(0);
        final String tier = context.asString(1);
        final int amount = context.asInt(2);

        final ItemStack item = this.plugin.getRobotManager().toItem(tier);

        item.setAmount(amount);

        player.getInventory().addItem(item);

        this.plugin.getMessageCache().sendMessage(player, "messages.robot-received", new PlaceholderReplacer()
                .add("%robot%", item.getItemMeta().getDisplayName())
                .add("%amount%", String.valueOf(amount))
        );
    }
}
