package org.evokedev.evokerobots.npc.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.evokedev.api.NPCService;
import org.evokedev.evokerobots.npc.impl.CitizensNPCService;
import org.evokedev.evokerobots.npc.impl.ServersNPCService;

@AllArgsConstructor @Getter
public enum NPCServiceType {

    SERVERSNPC(new ServersNPCService()),
    CITIZENS(new CitizensNPCService());

    private final NPCService<?> service;
}
