package org.evokedev.api;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface NPCService<N> {

    String getProviderName();

    void createNPC(final int npcId, final Location location, final String name);

    void setSkin(final int npcId, final String skin);

    void dispose(final int npcId);

    void teleport(final int npcId, final Location location);

    void playSwingAnimation(final int npcId);

    N get(final int npcId);

    void setEquipment(final int npcId, final ItemStack[] equipment);

    Location getLocation(final int npcId);
}
