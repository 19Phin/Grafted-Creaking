package net.dialingspoon.grafted_creaking.neoforge;

import net.dialingspoon.grafted_creaking.GraftedCreaking;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

@Mod(GraftedCreaking.MOD_ID)
public final class GraftedCreakingNeoForge {
    public GraftedCreakingNeoForge() {
        ModAttachments.register(ModLoadingContext.get().getActiveContainer().getEventBus());
    }
}
