package org.evokedev.evokerobots.npc.impl;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.evokedev.api.NPCService;

import java.util.UUID;

public final class CitizensNPCService implements NPCService<NPC> {

    @Override
    public String getProviderName() {
        return "Citizens";
    }

    @Override
    public void createNPC(final int npcId, final Location location, final String name) {
        final net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().createNPC(
            EntityType.PLAYER,
                UUID.randomUUID(),
                npcId,
                name
        );

        npc.setFlyable(true);
        npc.setProtected(true);
        npc.setUseMinecraftAI(false);
        npc.spawn(location);
    }

    @Override
    public void setSkin(final int npcId, final String skin) {
        this.get(npcId).getOrAddTrait(SkinTrait.class).setSkinName(skin);
    }

    @Override
    public void dispose(final int npcId) {
        CitizensAPI.getNPCRegistry().getById(npcId).destroy();
    }

    @Override
    public void teleport(final int npcId, final Location location) {
        this.get(npcId).teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void playSwingAnimation(final int npcId) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            PlayerAnimation.ARM_SWING.play(player, 8);
        }
    }

    @Override
    public NPC get(final int npcId) {
        return CitizensAPI.getNPCRegistry().getById(npcId);
    }

    @Override
    public void setEquipment(final int npcId, final ItemStack[] equipment) {
        final NPC npc = this.get(npcId);
        final Equipment trait = npc.getOrAddTrait(Equipment.class);

        trait.set(Equipment.EquipmentSlot.HELMET, equipment[0]);
        trait.set(Equipment.EquipmentSlot.CHESTPLATE, equipment[1]);
        trait.set(Equipment.EquipmentSlot.LEGGINGS, equipment[2]);
        trait.set(Equipment.EquipmentSlot.BOOTS, equipment[3]);
        trait.set(Equipment.EquipmentSlot.HAND, equipment[4]);
    }

    @Override
    public Location getLocation(int npcId) {
        return this.get(npcId).getStoredLocation();
    }
}
