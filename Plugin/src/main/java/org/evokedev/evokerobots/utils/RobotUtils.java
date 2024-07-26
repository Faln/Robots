package org.evokedev.evokerobots.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.type.RobotType;
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

    public String getTier(final ItemStack item) {
        return NBTEditor.getString(item, ROBOT_TIER);
    }



}
