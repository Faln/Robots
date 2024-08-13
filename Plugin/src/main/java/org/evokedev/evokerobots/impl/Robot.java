package org.evokedev.evokerobots.impl;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.eclipse.collections.api.factory.Maps;
import org.evokedev.api.PriceService;
import org.evokedev.evokerobots.type.RobotType;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.stormdev.storage.id.Id;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public final class Robot {

    private final Map<RobotUpgradeType, Integer> upgrades = new EnumMap<>(RobotUpgradeType.class);
    private final Map<Material, Integer> storage = Maps.mutable.empty();

    private final int npcId;
    private final UUID owner;
    @Id
    private final Location location;
    private final String tier;
    private final RobotType robotType;

    private double itemCount = 0;
    private long nextTick = System.currentTimeMillis();

    public int getUpgrade(final RobotUpgradeType upgrade) {
        return this.upgrades.getOrDefault(upgrade, 1);
    }

    public void upgrade(final RobotUpgradeType upgrade) {
        this.upgrades.put(upgrade, this.getUpgrade(upgrade) + 1);
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public double getSellPrice() {
        double sum = 0;

        final PriceService service = Bukkit.getServicesManager().load(PriceService.class);

        if (service == null) {
            return sum;
        }

        for (var entry : this.storage.entrySet()) {
            sum += service.getSellPrice(entry.getKey(), entry.getValue());
        }

        return sum;
    }

    public void clearContents() {
        this.storage.clear();
        this.itemCount = 0;
    }

}
