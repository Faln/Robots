package org.evokedev.evokerobots.npc.provider.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import io.github.gonalez.znpcs.ServersNPC;
import io.github.gonalez.znpcs.npc.NPC;
import io.github.gonalez.znpcs.npc.NPCType;
import io.github.gonalez.znpcs.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.evokedev.evokerobots.npc.provider.NPCProvider;

import java.util.UUID;

public final class ZNPC implements NPCProvider<NPC> {

    @Override
    public String getProviderName() {
        return "znpcs";
    }

    @Override
    public void createNPC(final int npcId, final Location location, final String name, final UUID id) {
        final io.github.gonalez.znpcs.npc.NPC npc = ServersNPC.createNPC(
                npcId,
                NPCType.PLAYER,
                location,
                name
        );

        npc.spawn(ZUser.find(id));
    }

    @Override
    public void dispose(final int npcId) {
        ServersNPC.deleteNPC(npcId);
    }

    @Override
    public void teleport(final int npcId, final Location location) {
        this.get(npcId).setLocation(location, false);
    }

    @Override
    public void playSwingAnimation(final int npcId) {
        final WrapperPlayServerEntityAnimation animation = new WrapperPlayServerEntityAnimation(npcId, WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM);

        for (final Player player : Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, animation);
        }
    }

    @Override
    public NPC get(final int npcId) {
        return io.github.gonalez.znpcs.npc.NPC.find(npcId);
    }
}
