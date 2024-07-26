package org.evokedev.evokerobots.upgrade;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class RobotUpgrade<V> {

    protected final Map<Integer, UpgradeDTO<V>> levels;
    protected final RobotUpgradeType type;

    @AllArgsConstructor @Getter
    public static class UpgradeDTO<V> {

        private int level;
        private final V value;
        private final double cost;

    }

}
