package org.evokedev.evokerobots.npc.provider.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import io.github.gonalez.znpcs.ServersNPC;
import io.github.gonalez.znpcs.npc.*;
import io.github.gonalez.znpcs.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Maps;
import org.evokedev.evokerobots.npc.provider.NPCProvider;

import java.util.Map;
import java.util.UUID;

public final class ZNPC implements NPCProvider<NPC> {

    @Override
    public String getProviderName() {
        return "znpcs";
    }

    @Override
    public void createNPC(final int npcId, final Location location, final String name) {
        ServersNPC.createNPC(
                npcId,
                NPCType.PLAYER,
                location,
                name
        );
    }

    @Override
    public void setSkin(final int npcId, final String skin) {
        this.get(npcId).getNpcPojo().setSkin(skin);
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

    @Override
    public void setEquipment(final int npcId, final ItemStack[] equipment) {
        final Map<ItemSlot, ItemStack> itemMap = Maps.mutable.empty();

        itemMap.put(ItemSlot.HELMET, equipment[0]);
        itemMap.put(ItemSlot.CHESTPLATE, equipment[1]);
        itemMap.put(ItemSlot.LEGGINGS, equipment[2]);
        itemMap.put(ItemSlot.BOOTS, equipment[3]);
        itemMap.put(ItemSlot.HAND, equipment[4]);

        final NPC npc = this.get(npcId);

        npc.getNpcPojo().withNpcEquip(itemMap);

        for (final ZUser player : npc.getViewers()) {
            npc.sendEquipPackets(player);
        }
    }

    @Override
    public Location getLocation(final int npcId) {
        return this.get(npcId).getLocation();
    }
}
