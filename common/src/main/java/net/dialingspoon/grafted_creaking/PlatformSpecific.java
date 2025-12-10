package net.dialingspoon.grafted_creaking;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.monster.creaking.Creaking;

public final class PlatformSpecific {
    @ExpectPlatform
    public static int getVariant(Creaking creaking, boolean second) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setVariant(Creaking creaking, CreakingVariant variant, boolean second) {
        throw new AssertionError();
    }
}
