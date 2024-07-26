package org.evokedev.evokerobots.storage;

import org.bukkit.Location;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.stormdev.storage.credentials.Credentials;
import org.stormdev.storage.sql.SQLStorage;

public final class RobotSQLStorage extends SQLStorage<Location, Robot> {

    public RobotSQLStorage(final EvokeRobots plugin) {
        super(Location.class, Robot.class, "evoke_robots", Credentials.from(plugin.getConfig("settings"), "robot-storage"));
    }

}
