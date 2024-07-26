package org.evokedev.evokerobots.stacker.provider.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.stacker.provider.StackProvider;

import java.util.Collection;
import java.util.Collections;

public class EmptyStackerProvider implements StackProvider {

    @Override
    public void remove(LivingEntity entity, int amount) {

    }

    @Override
    public Collection<ItemStack> getDrops(final LivingEntity entity) {
        return Collections.emptyList();
    }

}
