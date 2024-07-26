package org.evokedev.evokerobots.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

import java.util.*;

@UtilityClass
public final class CropUtils {

    public static final Map<Material, Integer> CROPS = new EnumMap<>(Material.class);
    public static final Set<Material> ALL_CROPS = new HashSet<>();

    static {
        CROPS.put(Material.WHEAT_SEEDS, 7);
        CROPS.put(Material.WHEAT, 7);
        CROPS.put(Material.CARROTS, 7);
        CROPS.put(Material.POTATOES, 7);
        CROPS.put(Material.NETHER_WART, 3);
        CROPS.put(Material.BEETROOT, 3);
        CROPS.put(Material.SUGAR_CANE, 15);

        ALL_CROPS.addAll(CROPS.keySet());
        ALL_CROPS.addAll(EnumSet.of(Material.MELON, Material.PUMPKIN, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS));
    }

    public static boolean isMaxAge(final Block block) {
        if (!(block.getBlockData() instanceof Ageable)) {
            return true;
        }

        final Ageable ageable = (Ageable) block.getBlockData();

        return ageable.getAge() == ageable.getMaximumAge();
    }

}
