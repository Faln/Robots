package org.evokedev.evokerobots.stacker.provider.impl;

import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.evokedev.evokerobots.stacker.provider.StackProvider;

import java.util.Collection;
import java.util.Collections;

public final class RoseStackerProvider implements StackProvider {

    @Override
    public void remove(final LivingEntity entity, final int amount) {
        final StackedEntity stackedEntity = RoseStackerAPI.getInstance().getStackedEntity(entity);

        if (stackedEntity == null) {
            return;
        }

        stackedEntity.killPartialStack(null, amount);
    }

    @Override
    public Collection<ItemStack> getDrops(final LivingEntity entity) {
        final StackedEntity stackedEntity = RoseStackerAPI.getInstance().getStackedEntity(entity);
        return stackedEntity == null ? Collections.emptyList() : stackedEntity
                .calculateEntityDrops(Collections.singleton(entity), 1, true, 1)
                .getDrops();
    }
}
