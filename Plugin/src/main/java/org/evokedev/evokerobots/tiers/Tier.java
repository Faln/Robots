package org.evokedev.evokerobots.tiers;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.type.RobotType;
import org.stormdev.builder.ItemBuilder;

import java.util.Objects;

/**
 * POJO for rarities/tiers of {@code Robot}s
 */
@AllArgsConstructor @Getter
public final class Tier {

    public static Tier of(
            final String name,
            final RobotType type,
            final String display,
            final double speed,
            final double storage,
            final int radius,
            final ItemBuilder robotItem,
            final ItemStack[] equipment
    ) {
        Objects.requireNonNull(name, "Tier must have a identifier");
        Objects.requireNonNull(display, "Tier must have a display");
        Preconditions.checkArgument(speed > 0.0, "Cannot have a interval <= 0");
        Preconditions.checkArgument(radius > 0);

        return new Tier(name, type, display, speed, storage, radius, robotItem, equipment);
    }

    @NonNull
    private final String name;
    private final RobotType type;
    @NonNull
    private final String display;
    private final double speed;
    private final double storage;
    private final int radius;
    private final ItemBuilder robotItem;
    private final ItemStack[] equipment;

}
