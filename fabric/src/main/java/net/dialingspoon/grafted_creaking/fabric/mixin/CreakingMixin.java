package net.dialingspoon.grafted_creaking.fabric.mixin;

import net.dialingspoon.grafted_creaking.fabric.GraftedCreakingFabric;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.creaking.Creaking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creaking.class)
public final class CreakingMixin {
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void grafted_creaking$initTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(GraftedCreakingFabric.VARIANT, 8);
        builder.define(GraftedCreakingFabric.VARIANT_2, 8);
    }
}
