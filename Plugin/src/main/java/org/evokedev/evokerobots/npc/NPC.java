package org.evokedev.evokerobots.npc;

import org.bukkit.Bukkit;
import org.evokedev.evokerobots.npc.provider.NPCProvider;
import org.evokedev.evokerobots.npc.provider.impl.CitizensNPC;
import org.evokedev.evokerobots.npc.provider.impl.ZNPC;

public final class NPC {

    private static NPCProvider<?> npcProvider;

    private NPC() {}

    private static NPCProvider<?> get() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ServersNPC")) {
            Bukkit.getLogger().info("Using ServersNPC as NPC Provider");
            return new ZNPC();
        } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("Citizens")) {
            Bukkit.getLogger().info("Using Citizens as NPC Provider");
            return new CitizensNPC();
        }

        throw new IllegalStateException("No NPC Provider found");
    }

    public static NPCProvider<?> getNpcProvider() {
        if (npcProvider == null) {
            npcProvider = get();
        }

        return npcProvider;
    }
}
