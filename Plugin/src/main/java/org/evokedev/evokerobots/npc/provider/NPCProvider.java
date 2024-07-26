package org.evokedev.evokerobots.npc.provider;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

public interface NPCProvider<N> {

    String getProviderName();

    void createNPC(final int npcId, final Location location, final String name, @Nullable final UUID id);

    void dispose(final int npcId);

    void teleport(final int npcId, final Location location);

    void playSwingAnimation(final int npcId);

    N get(final int npcId);

    void setEquipment(final ItemStack helmet, final ItemStack chest, final ItemStack legs, final ItemStack boots);
    
}
