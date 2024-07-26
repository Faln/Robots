package org.evokedev.evokerobots.utils;

import org.eclipse.collections.impl.factory.Maps;
import org.stormdev.abstracts.Registry;

import java.util.Map;

public abstract class EclipseRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> registry = Maps.mutable.empty();

    @Override
    public Map<K, V> getRegistry() {
        return this.registry;
    }
}
