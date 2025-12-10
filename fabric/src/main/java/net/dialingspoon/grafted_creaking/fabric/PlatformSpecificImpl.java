package net.dialingspoon.grafted_creaking.fabric;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.minecraft.world.entity.monster.creaking.Creaking;

public final class PlatformSpecificImpl {
    private PlatformSpecificImpl() {
    }

    public static int getVariant(Creaking creaking, boolean second) {
        return second
                ? creaking.getEntityData().get(GraftedCreakingFabric.VARIANT_2)
                : creaking.getEntityData().get(GraftedCreakingFabric.VARIANT);
    }

    public static void setVariant(Creaking creaking, CreakingVariant variant, boolean second) {
        int id = variant.getId() & 255;
        if (second) {
            creaking.getEntityData().set(GraftedCreakingFabric.VARIANT_2, id);
        } else {
            creaking.getEntityData().set(GraftedCreakingFabric.VARIANT, id);
        }
    }
}
