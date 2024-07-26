package org.evokedev.evokerobots.tasks;

import org.evokedev.evokerobots.EvokeRobots;
import org.stormdev.abstracts.CommonTask;

public final class RobotTickTask extends CommonTask<EvokeRobots> {

    public RobotTickTask(final EvokeRobots plugin) {
        super(plugin, 20);
    }

    @Override
    public void run() {
        this.plugin.getRobotManager().tickRobots();
    }
}
