package org.evokedev.evokerobots.upgrade.registry;

import lombok.AllArgsConstructor;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.upgrade.RobotUpgrade;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.evokedev.evokerobots.upgrade.impl.SpeedRobotUprade;
import org.evokedev.evokerobots.upgrade.impl.StorageRobotUpgrade;
import org.evokedev.evokerobots.utils.EclipseRegistry;
import org.stormdev.files.CommonConfig;

@AllArgsConstructor
public final class RobotUpgradeRegistry extends EclipseRegistry<Class<? extends RobotUpgrade<?>>, RobotUpgrade<?>> {

    private final EvokeRobots plugin;

    public void load() {
        this.getRegistry().clear();

        final CommonConfig config = this.plugin.getConfig("upgrades");

        this.register(SpeedRobotUprade.class, SpeedRobotUprade.of(config));
        this.register(StorageRobotUpgrade.class, StorageRobotUpgrade.of(config));
    }

    public <U extends RobotUpgrade<?>> U getUpgrade(final Class<U> clazz) {
        return (U) this.getRegistry().get(clazz);
    }
}
