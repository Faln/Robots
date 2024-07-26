package org.evokedev.evokerobots.storage;

import org.bukkit.Location;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.stormdev.files.Files;
import org.stormdev.storage.json.JsonStorage;

public final class RobotJsonStorage extends JsonStorage<Location, Robot> {

    public RobotJsonStorage(final EvokeRobots plugin) {
        super(Files.file("data.json", plugin), Robot.class);
    }

}
