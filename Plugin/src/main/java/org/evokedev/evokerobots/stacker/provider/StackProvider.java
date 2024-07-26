package org.evokedev.evokerobots.stacker.provider;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface StackProvider {

    void remove(final LivingEntity entity, final int amount);

    Collection<ItemStack> getDrops(final LivingEntity entity);

}
