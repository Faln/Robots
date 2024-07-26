package org.evokedev.evokerobots.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.EvokeRobots;
import org.evokedev.evokerobots.impl.Robot;
import org.evokedev.evokerobots.npc.EntityIdHandler;
import org.evokedev.evokerobots.npc.NPC;
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
import org.stormdev.utils.WordUtils;

@RequiredArgsConstructor
@Getter
public class RobotManager {

    private static final String TIER_PLACEHOLDER = "%tier%";
    private static final String SPEED_PLACEHOLDER = "%speed%";
    private static final String STORAGE_PLACEHOLDER = "%storage%";
    private static final String TYPE_PLACEHOLDER = "%type%";
    private static final String ROBOT_NAME = "%tier% %type% Robot";

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
                .addPlaceholder(STORAGE_PLACEHOLDER, FormatUtils.formatComma(tier.getStorage()))
                .addPlaceholder(TYPE_PLACEHOLDER, tier.getType().name())
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier, RobotUtils.ROBOT_TIER);
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
                .addPlaceholder(TIER_PLACEHOLDER, robot.getTier())
                .addPlaceholder(SPEED_PLACEHOLDER, String.valueOf(speed.getValue()))
                .addPlaceholder(STORAGE_PLACEHOLDER, FormatUtils.formatComma(storage.getValue()))
                .addPlaceholder(TYPE_PLACEHOLDER, robot.getRobotType().getFormattedName())
        );

        item = NBTEditor.set(item, true, RobotUtils.ROBOT_NBT);
        item = NBTEditor.set(item, tier, RobotUtils.ROBOT_TIER);
        item = NBTEditor.set(item, speed, RobotUtils.ROBOT_SPEED);
        item = NBTEditor.set(item, storage, RobotUtils.ROBOT_STORAGE);
        item = NBTEditor.set(item, robot.getRobotType().name(), RobotUtils.ROBOT_TYPE);

        return item;
    }

    public void initRobot(final Player player, final ItemStack item, final Location location) {
        final String tierInString = RobotUtils.getTier(item);
        final Tier tier = this.plugin.getTierRegistry().getRegistry().get(tierInString);
        final RobotType type = RobotUtils.getType(item);
        final int npcId = EntityIdHandler.getInstance().getNewId();

        NPC.getNpcProvider().createNPC(
                npcId,
                location,
                tier.getDisplay(),
                player.getUniqueId()
        );

        final Robot robot = new Robot(npcId, player.getUniqueId(), location, tierInString, type);

        this.plugin.getRobotStorage().save(robot);
    }

    public void dispose(final Robot robot) {
        NPC.getNpcProvider().dispose(robot.getNpcId());
        robot.getStorage().clear();
    }

    public void tickRobots() {
        for (final Robot robot : this.plugin.getRobotStorage().cache().values()) {
            this.tickRobot(robot);
        }
    }

    public void tickRobot(final Robot robot) {
        if (System.currentTimeMillis() > robot.getNextTick()) {
            return;
        }

        if (this.isStorageFull(robot)) {
            return;
        }

        val drops = robot.getRobotType().getFunction().apply(robot.getLocation());
        val storageUpgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);
        val tier = this.plugin.getTierRegistry().getRegistry().get(robot.getTier());
        val max = tier.getStorage() * storageUpgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.STORAGE)).getValue();

        for (val drop : drops.entrySet()) {
            if (robot.getItemCount() >= max) {
                break;
            }

            if (drop.getValue() + robot.getItemCount() > max) {
                break;
            }

            robot.getStorage().putAll(drops);
            robot.setItemCount(robot.getStorage().values().stream().mapToInt(Integer::intValue).sum());
        }

        val upgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(SpeedRobotUprade.class);
        val speed = tier.getSpeed() / upgrade.getLevels().get(robot.getUpgrade(RobotUpgradeType.SPEED)).getValue();

        robot.setNextTick((long) (System.currentTimeMillis() + speed * 1000));

        NPC.getNpcProvider().playSwingAnimation(robot.getNpcId());
    }

    public boolean isStorageFull(final Robot robot) {
        val upgrade = this.plugin.getRobotUpgradeRegistry().getUpgrade(StorageRobotUpgrade.class);

        return robot.getItemCount() >= upgrade.getLevels()
                .get(robot.getUpgrades()
                .get(RobotUpgradeType.STORAGE))
                .getValue();
    }
}