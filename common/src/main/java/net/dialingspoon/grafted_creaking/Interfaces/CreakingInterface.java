package net.dialingspoon.grafted_creaking.Interfaces;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.minecraft.world.entity.LivingEntity;

public interface CreakingInterface {
    boolean grafted_creaking$shouldAttack(LivingEntity entity);

    boolean grafted_creaking$hasVariant(CreakingVariant variant);
}
