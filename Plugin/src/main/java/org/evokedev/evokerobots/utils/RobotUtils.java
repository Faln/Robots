package org.evokedev.evokerobots.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.type.RobotType;
import org.evokedev.evokerobots.upgrade.RobotUpgradeType;
import org.stormdev.utils.NBTEditor;

@UtilityClass
public class RobotUtils {

    public static final String ROBOT_SPEED = "ROBOT_SPEED_UPGRADE";
    public static final String ROBOT_STORAGE = "ROBOT_STORAGE_UPGRADE";

    public static final String ROBOT_NBT = "ROBOT_NBT";
    public static final String ROBOT_TYPE = "ROBOT_TYPE";
    public static final String ROBOT_TIER = "ROBOT_TIER";

    public boolean isRobot(final ItemStack item) {
        return NBTEditor.contains(item, ROBOT_NBT);
    }

    public RobotType getType(final ItemStack item) {
        return RobotType.valueOf(NBTEditor.getString(item, ROBOT_TYPE).toUpperCase());
    }

    public int getUpgradeLevel(final RobotUpgradeType type, final ItemStack item) {
        switch (type) {
            case SPEED:
                return NBTEditor.getInt(item, ROBOT_SPEED);
            case STORAGE:
                return NBTEditor.getInt(item, ROBOT_STORAGE);
            default:
                throw new IllegalArgumentException("unknown upgrade type");
        }
    }

    public String getTier(final ItemStack item) {
        return NBTEditor.getString(item, ROBOT_TIER);
    }



}
