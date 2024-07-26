package org.evokedev.evokerobots.upgrade.impl;

import org.eclipse.collections.api.factory.Maps;
import org.evokedev.evokerobots.upgrade.RobotUpgrade;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.stormdev.files.CommonConfig;

import java.util.Map;

public final class StorageRobotUpgrade extends RobotUpgrade<Double> {

    public static StorageRobotUpgrade of(final CommonConfig config) {
        final Map<Integer, UpgradeDTO<Double>> levels = Maps.mutable.empty();

        for (final String level : config.getSectionKeys("upgrades.STORAGE")) {
            final int levelInt = Integer.parseInt(level);

            levels.put(levelInt, new UpgradeDTO<>(
                    levelInt,
                    config.getDouble("upgrades.STORAGE." + level + ".value"),
                    config.getDouble("upgrades.STORAGE." + level + ".cost")
            ));
        }

        return new StorageRobotUpgrade(levels, RobotUpgradeType.STORAGE);
    }

    private StorageRobotUpgrade(final Map<Integer, UpgradeDTO<Double>> levels, final RobotUpgradeType type) {
        super(levels, type);
    }

}
