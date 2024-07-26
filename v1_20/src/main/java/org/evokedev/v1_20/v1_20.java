package org.evokedev.v1_20;

import lombok.SneakyThrows;
import net.minecraft.world.entity.Entity;
import org.evokedev.api.EntityIdProvider;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public final class v1_20 implements EntityIdProvider {

    private final Field field;

    public v1_20() {
        try {
            this.field = Entity.class.getDeclaredField("d");
            this.field.setAccessible(true);
        } catch (final NoSuchFieldException e) {
            throw new IllegalStateException("Entity id provider not found");
        }
    }

    @Override
    @SneakyThrows
    public int get() {
        return ((AtomicInteger) this.field.get(null)).getAndIncrement();
    }
}
