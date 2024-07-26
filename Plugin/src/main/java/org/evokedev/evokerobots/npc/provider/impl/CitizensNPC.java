package org.evokedev.evokerobots.npc.provider.impl;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.evokedev.evokerobots.npc.provider.NPCProvider;

import java.util.UUID;

public final class CitizensNPC implements NPCProvider<NPC> {

    @Override
    public String getProviderName() {
        return "Citizens";
    }

    @Override
    public void createNPC(final int npcId, final Location location, final String name, final UUID id) {
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
}