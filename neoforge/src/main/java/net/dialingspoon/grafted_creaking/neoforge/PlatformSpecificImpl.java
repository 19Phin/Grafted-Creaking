package net.dialingspoon.grafted_creaking.neoforge;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.minecraft.world.entity.monster.creaking.Creaking;

public final class PlatformSpecificImpl {
    public static int getVariant(Creaking creaking, boolean second) {
        return second
                ? creaking.getData(ModAttachments.SECOND_VARIANT)
                : creaking.getData(ModAttachments.VARIANT);
    }

    public static void setVariant(Creaking creaking, CreakingVariant variant, boolean second) {
        int id = variant.getId() & 255;
        if (second) {
            creaking.setData(ModAttachments.SECOND_VARIANT, variant.getId() & 255);
        } else {
            creaking.setData(ModAttachments.VARIANT, variant.getId() & 255);
        }
    }
}
