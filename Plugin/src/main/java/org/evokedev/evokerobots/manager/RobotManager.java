package org.evokedev.evokerobots.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evokedev.api.NPCService;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.npc.EntityIdHandler;
import org.evokedev.evokerobots.tiers.Tier;
import org.evokedev.evokerobots.type.RobotType;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.evokedev.evokerobots.upgrade.impl.SpeedRobotUprade;
import org.evokedev.evokerobots.upgrade.impl.StorageRobotUpgrade;
import org.evokedev.evokerobots.utils.FormatUtils;
import org.evokedev.evokerobots.utils.RobotUtils;
import org.stormdev.chat.PlaceholderReplacer;
import org.stormdev.files.CommonConfig;
import org.stormdev.menus.v2.item.MenuItemBuilder;
import org.stormdev.utils.NBTEditor;

@RequiredArgsConstructor
@Getter
public class RobotManager {

    private static final String TIER_PLACEHOLDER = "%tier%";
    private static final String SPEED_PLACEHOLDER = "%speed%";
    private static final String SPEED_LEVEL_PLACEHOLDER = "%speed-level%";
    private static final String STORAGE_PLACEHOLDER = "%storage%";
    private static final String STORAGE_LEVEL_PLACEHOLDER = "%storage-level%";
    private static final String TYPE_PLACEHOLDER = "%type%";

    private final EvokeRobots plugin;

    private MenuItemBuilder pickupMenuItem, upgradesMenuItem, sellMenuItem;

    public void load() {
        final CommonConfig config = this.plugin.getConfig("menus");

        this.pickupMenuItem = config.getMenuItemBuilder("robot.pickup");
        this.upgradesMenuItem = config.getMenuItemBuilder("robot.upgrades");
        this.sellMenuItem = config.getMenuItemBuilder("robot.sell");
    }

    public ItemStack toItem(final String tier) {
        return this.toItem(this.plugin.getTierRegistry().getRegistry().get(tier));
    }

    public ItemStack toItem(final Tier tier) {
        ItemStack item = tier.getRobotItem().parse(new PlaceholderReplacer()
                .addPlaceholder(TIER_PLACEHOLDER, tier.getName())
                .addPlaceholder(SPEED_PLACEHOLDER, String.valueOf(tier.getSpeed()))
                .addPlaceholder(SPEED_LEVEL_PLACEHOLDER, "1")
                .addPlaceholder(STORAGE_PLACEHOLDER, FormatUtils.formatComma(tier.getStorage()))
                .addPlaceholder(STORAGE_LEVEL_PLACEHOLDER, "1")
                .addPlaceholder(TYPE_PLACEHOLDER, tier.getType().name())
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier.getName(), RobotUtils.ROBOT_TIER);
        item = NBTEditor.set(item, tier.getSpeed(), RobotUtils.ROBOT_SPEED);
        item = NBTEditor.set(item, tier.getStorage(), RobotUtils.ROBOT_STORAGE);
        item = NBTEditor.set(item, tier.getType().name(), RobotUtils.ROBOT_TYPE);

        return item;
    }

    public ItemStack toItem(final Robot robot) {
        val tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());
        val speedUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(SpeedRobotUprade.class);
        val storageUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);
        val speed = speedUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED));
        val storage = storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE));

        ItemStack item = tier.getRobotItem().parse(new PlaceholderReplacer()
                .addPlaceholder(TIER_PLACEHOLDER, tier.getDisplay())
                .addPlaceholder(SPEED_PLACEHOLDER, String.valueOf(speed.getValue()))
                .addPlaceholder(SPEED_LEVEL_PLACEHOLDER, String.valueOf(speed.getLevel()))
                .addPlaceholder(STORAGE_PLACEHOLDER, FormatUtils.formatComma(storage.getValue()))
                .addPlaceholder(STORAGE_LEVEL_PLACEHOLDER, FormatUtils.formatComma(storage.getLevel()))
                .addPlaceholder(TYPE_PLACEHOLDER, robot.getRobotType().getFormattedName())
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier.getName(), RobotUtils.ROBOT_TIER);
        item = NBTEditor.set(item, speed.getLevel(), RobotUtils.ROBOT_SPEED);
        item = NBTEditor.set(item, storage.getLevel(), RobotUtils.ROBOT_STORAGE);
        item = NBTEditor.set(item, robot.getRobotType().name(), RobotUtils.ROBOT_TYPE);

        return item;
    }

    public void initRobot(final Player player, final ItemStack item, final Location location) {
        final String tierInString = RobotUtils.getTier(item);
        final Tier tier = this.plugin.getTierRegistry().getRegistry().get(tierInString);
        final RobotType type = RobotUtils.getType(item);
        final int npcId = EntityIdHandler.getInstance().getNewId();

        final NPCService<?> service = this.plugin.getServer().getServicesManager().load(NPCService.class);

        if (service != null) {
            service.createNPC(npcId, location, tier.getDisplay());
            service.setEquipment(npcId, tier.getEquipment());
        }

        final Robot robot = new Robot(npcId, player.getUniqueId(), location, tierInString, type);

        robot.getUpgrades().putIfAbsent(RobotUpgradeType.SPEED, 1);
        robot.getUpgrades().putIfAbsent(RobotUpgradeType.STORAGE, 1);
        robot.getUpgrades().put(RobotUpgradeType.SPEED, RobotUtils.getUpgradeLevel(RobotUpgradeType.SPEED, item));
        robot.getUpgrades().put(RobotUpgradeType.STORAGE, RobotUtils.getUpgradeLevel(RobotUpgradeType.STORAGE, item));

        this.plugin.getRobotStorage().save(robot);
    }

    public void dispose(final Robot robot) {
        final NPCService<?> service = this.plugin.getServer().getServicesManager().load(NPCService.class);

        if (service != null) {
            service.dispose(robot.getNpcId());
        }

        robot.getStorage().clear();

        this.plugin.getRobotStorage().remove(robot.getLocation());
    }

    public void tickRobots() {
        for (final Robot robot : this.plugin.getRobotStorage().cache().values()) {
            this.tickRobot(robot);
        }
    }

    public void tickRobot(final Robot robot) {
        if (System.currentTimeMillis() < robot.getNextTick()) {
            return;
        }

        val tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());
        val drops = robot.getRobotType().getFunction().apply(robot.getLocation(), tier);
        val storageUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);
        val max = tier.getStorage() * storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE)).getValue();

        if (robot.getItemCount() >= max) {
            return;
        }

        for (val drop : drops.entrySet()) {
            if (robot.getItemCount() >= max) {
                break;
            }

            if (drop.getValue() + robot.getItemCount() > max) {
                break;
            }

            robot.getStorage().putIfAbsent(drop.getKey(), 0);
            robot.getStorage().put(drop.getKey(), robot.getStorage().get(drop.getKey()) + drop.getValue());
            robot.setItemCount(robot.getItemCount() + drop.getValue());
        }

        val upgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(SpeedRobotUprade.class);
        val speed = tier.getSpeed() / upgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED)).getValue();

        robot.setNextTick((long) (System.currentTimeMillis() + speed * 1000));

        final NPCService<?> service = this.plugin.getServer().getServicesManager().load(NPCService.class);

        if (service != null) {
            service.playSwingAnimation(robot.getNpcId());
        }
    }
}
