package org.evokedev.evokerobots.upgrade.impl;

import org.eclipse.collections.api.factory.Maps;
import org.evokedev.evokerobots.upgrade.RobotUpgrade;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.stormdev.files.CommonConfig;

import java.util.Map;

public final class SpeedRobotUprade extends RobotUpgrade<Double> {

    public static SpeedRobotUprade of(final CommonConfig config) {
        final Map<Integer, UpgradeDTO<Double>> levels = Maps.mutable.empty();

        for (final String level : config.getSectionKeys("upgrades.SPEED")) {
            final int levelInt = Integer.parseInt(level);

            levels.put(levelInt, new UpgradeDTO<>(
                    levelInt,
                    config.getDouble("upgrades.SPEED." + level + ".value"),
                    config.getDouble("upgrades.SPEED." + level + ".cost"))
            );
        }

        return new SpeedRobotUprade(levels, RobotUpgradeType.SPEED);
    }

    private SpeedRobotUprade(final Map<Integer, UpgradeDTO<Double>> levels, final RobotUpgradeType type) {
        super(levels, type);
    }
}
