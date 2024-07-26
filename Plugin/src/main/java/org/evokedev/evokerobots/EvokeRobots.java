package org.evokedev.evokerobots;

import lombok.Getter;
import org.bukkit.Location;
import org.evokedev.evokerobots.commands.RobotCommand;
import org.evokedev.evokerobots.commands.subcommands.RobotGiveSubCommand;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.listeners.RobotListener;
import org.evokedev.evokerobots.manager.RobotManager;
import org.evokedev.evokerobots.storage.RobotJsonStorage;
import org.evokedev.evokerobots.storage.RobotSQLStorage;
import org.evokedev.evokerobots.tasks.RobotTickTask;
import org.evokedev.evokerobots.tiers.registry.TierRegistry;
import org.evokedev.evokerobots.upgrade.registry.RobotUpgradeRegistry;
import org.stormdev.CommonPlugin;
import org.stormdev.files.MessageCache;
import org.stormdev.storage.common.CommonStorageImpl;
import org.stormdev.storage.type.StorageType;

@Getter
public final class EvokeRobots extends CommonPlugin<EvokeRobots> {

    private final MessageCache messageCache = new MessageCache(this.getConfig("lang"));

    private final TierRegistry tierRegistry = new TierRegistry(this);
    private final RobotUpgradeRegistry robotUpgradeRegistry = new RobotUpgradeRegistry(this);

    private final RobotManager robotManager = new RobotManager(this);

    private final RobotTickTask robotTickTask = new RobotTickTask(this);

    private final RobotCommand robotCommand = new RobotCommand(this);

    private CommonStorageImpl<Location, Robot> robotStorage;

    @Override
    public void onEnable() {
        switch (StorageType.valueOf(this.getConfig("settings").getString("robot-storage.type"))) {
            case SQL:
                this.robotStorage = new CommonStorageImpl<>(new RobotSQLStorage(this));
                break;
            default:
            case JSON:
                this.robotStorage = new CommonStorageImpl<>(new RobotJsonStorage(this));
        }

        this.tierRegistry.load();
        this.robotUpgradeRegistry.load();
        this.robotManager.load();

        this.robotTickTask.run();

        this.robotCommand.register();
        this.robotCommand.register(
                new RobotGiveSubCommand(this)
        );

        new RobotListener(this);
    }

    @Override
    public void onDisable() {
        if (!this.robotTickTask.isCancelled()) {
            this.robotTickTask.cancel();
        }

        this.robotStorage.write();
        this.robotCommand.unregister();
    }
}
