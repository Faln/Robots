package org.evokedev.evokerobots.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Maps;
import org.evokedev.evokerobots.stacker.StackHandler;
import org.evokedev.evokerobots.tiers.Tier;
import org.evokedev.evokerobots.utils.CropUtils;
import org.evokedev.evokerobots.utils.OreUtils;
import org.stormdev.utils.Pair;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor @Getter
public enum RobotType {

    FARMER("Farmer", (location, tier) -> {
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY();
        final int blockZ = location.getBlockZ();
        final int radius = tier.getRadius();

        for (int x = blockX - radius; x < blockX + radius; x++) {
            for (int z = blockZ - radius; z < blockZ + radius; z++) {
                final Location loc = new Location(location.getWorld(), x, blockY, z);
                final Block block = loc.getBlock();

                if (block.getType() == Material.AIR || !(block.getBlockData() instanceof Ageable) || !CropUtils.isMaxAge(block)) {
                    continue;
                }

                final Map<Material, Integer> drops = Maps.mutable.empty();

                block.getDrops().forEach(itemStack -> {
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        return;
                    }

                    final Material material = itemStack.getType();

                    drops.putIfAbsent(material, 0);
                    drops.put(material, drops.get(material) + itemStack.getAmount());
                });

                final Ageable ageable = (Ageable) block.getBlockData();

                ageable.setAge(0);
                block.setBlockData(ageable);

                return drops;
            }
        }

        return Maps.mutable.empty();
    }),
    SLAYER("Slayer", (location, tier) -> {
        if (location.getWorld() == null) {
            return Maps.mutable.empty();
        }

        final int radius = tier.getRadius();

        for (final Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius, LivingEntity.class::isInstance)) {
            if (entity.getType() == EntityType.PLAYER || entity.getType() == EntityType.ARMOR_STAND) {
                continue;
            }

            final LivingEntity livingEntity = (LivingEntity) entity;

            StackHandler.getStackProvider().remove(livingEntity, 1);

            final Map<Material, Integer> drops = Maps.mutable.empty();

            for (final ItemStack item : StackHandler.getStackProvider().getDrops(livingEntity)) {
                final Material material = item.getType();

                drops.putIfAbsent(material, 0);
                drops.put(material, drops.get(material) + item.getAmount());
            }

            return drops;
        }

        return Maps.mutable.empty();
    }),
    MINER("Miner", (location, tier) -> {
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY();
        final int blockZ = location.getBlockZ();
        final int radius = tier.getRadius();

        for (int x = blockX - radius; x < blockX + radius; x++) {
            for (int z = blockZ - radius; z < blockZ + radius; z++) {
                final Location loc = new Location(location.getWorld(), x, blockY, z);
                final Block block = loc.getBlock();

                if (block.getType() == Material.AIR || !OreUtils.ORES.containsKey(block.getType())) {
                    continue;
                }

                final Map<Material, Integer> drops = Maps.mutable.empty();

                block.getDrops().forEach(itemStack -> {
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        return;
                    }

                    final Material material = itemStack.getType();

                    drops.putIfAbsent(material, 0);
                    drops.put(material, drops.get(material) + itemStack.getAmount());
                });

                block.setType(Material.AIR);

                return drops;
            }
        }

        return Maps.mutable.empty();
    });

    private final String formattedName;
    private final BiFunction<Location, Tier, Map<Material, Integer>> function;
}
