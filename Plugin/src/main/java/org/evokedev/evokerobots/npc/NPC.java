package org.evokedev.evokerobots.npc;

import org.bukkit.Bukkit;
import org.evokedev.evokerobots.npc.provider.NPCProvider;
import org.evokedev.evokerobots.npc.provider.impl.CitizensNPC;
import org.evokedev.evokerobots.npc.provider.impl.ZNPC;

public final class NPC {


    private NPC() {}

    private static final NPCProvider<?> NPC_PROVIDER;

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ServerNPC")) {
            NPC_PROVIDER = new ZNPC();
            Bukkit.getLogger().info("Using ZNPCs as NPC Provider");
        } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("Citizens")) {
            NPC_PROVIDER = new CitizensNPC();
            Bukkit.getLogger().info("Using Citizens as NPC Provider");
        } else {
            throw new IllegalStateException("No NPC Provider found");
        }
    }

    public static NPCProvider<?> getNpcProvider() {
        return NPC_PROVIDER;
    }
}
