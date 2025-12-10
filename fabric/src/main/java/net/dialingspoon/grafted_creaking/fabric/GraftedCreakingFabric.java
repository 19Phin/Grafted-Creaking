package net.dialingspoon.grafted_creaking.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.creaking.Creaking;

public final class GraftedCreakingFabric implements ModInitializer {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> VARIANT_2 = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.INT);

    @Override
    public void onInitialize() {
    }
}
