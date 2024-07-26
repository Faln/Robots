package org.evokedev.evokerobots.npc;

import org.evokedev.api.EntityIdProvider;
import org.evokedev.v1_20.v1_20;
import org.stormdev.utils.Version;

public final class EntityIdHandler {

    private static final EntityIdProvider ID_PROVIDER;
    private static EntityIdHandler instance;

    static {
        switch (Version.getCurrentVersion()) {
            case v1_20_R1:
            case v1_20_R2:
            case v_1_20_R3:
            default:
                ID_PROVIDER = new v1_20();
        }
    }

    private EntityIdHandler() {}

    public static EntityIdHandler getInstance() {
        if (instance == null) {
            instance = new EntityIdHandler();
        }

        return instance;
    }

    public int getNewId() {
        return ID_PROVIDER.get();
    }

}
