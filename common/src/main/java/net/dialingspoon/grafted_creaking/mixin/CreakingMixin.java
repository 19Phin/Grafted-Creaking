package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creaking.class)
public abstract class CreakingMixin extends Monster implements CreakingInterface {
    @Unique
    private static final EntityDataAccessor<Integer> grafted_creaking$VARIANT = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> grafted_creaking$VARIANT2 = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.INT);

    @Shadow
    public abstract BlockPos getHomePos();

    protected CreakingMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void initTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(grafted_creaking$VARIANT, 8);
        builder.define(grafted_creaking$VARIANT2, 8);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("Variant", grafted_creaking$getVariant(false));
        compound.putInt("Variant2", grafted_creaking$getVariant(true));
        super.addAdditionalSaveData(compound);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        Creaking creakingEntity = (Creaking) (Object) this;
        creakingEntity.getEntityData().set(grafted_creaking$VARIANT, compound.getInt("Variant").orElse(8));
        creakingEntity.getEntityData().set(grafted_creaking$VARIANT2, compound.getInt("Variant2").orElse(8));
    }

    @Inject(method = "setTransient", at = @At("TAIL"))
    protected void onInitialize(BlockPos blockPos, CallbackInfo ci) {
        CreakingVariant variant = CreakingVariant.PALE_OAK;
        CreakingVariant variant2 = CreakingVariant.PALE_OAK;

        if (((Creaking) (Object) this).isHeartBound()) {
            Direction.Axis axis = this.level().getBlockState(getHomePos()).getValue(CreakingHeartBlock.AXIS);
            variant = grafted_creaking$getVariantFromBlock(this.level().getBlockState(getHomePos().relative(axis.getNegative())));
            variant2 = grafted_creaking$getVariantFromBlock(this.level().getBlockState(getHomePos().relative(axis.getPositive())));
        }

        this.grafted_creaking$setVariant(variant, false);
        this.grafted_creaking$setVariant(variant2, true);

    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void defaultPale(CallbackInfo ci) {
        this.grafted_creaking$setVariant(CreakingVariant.PALE_OAK, false);
        this.grafted_creaking$setVariant(CreakingVariant.PALE_OAK, true);
    }

    @Override
    public int grafted_creaking$getVariant(boolean second) {
        SynchedEntityData data = this.getEntityData();
        return second ? data.get(grafted_creaking$VARIANT2) : data.get(grafted_creaking$VARIANT);
    }

    @Override
    public void grafted_creaking$setVariant(CreakingVariant variant, boolean second) {
        SynchedEntityData data = this.getEntityData();
        if (second) {
            data.set(grafted_creaking$VARIANT2, variant.getId() & 255);
        } else {
            data.set(grafted_creaking$VARIANT, variant.getId() & 255);
        }
    }

    @Unique
    public CreakingVariant grafted_creaking$getVariantFromBlock(BlockState wood) {
        CreakingVariant variant = CreakingVariant.PALE_OAK;

        if(wood.is(BlockTags.OAK_LOGS)) {
            variant = CreakingVariant.OAK;
        } else if(wood.is(BlockTags.SPRUCE_LOGS)) {
            variant = CreakingVariant.SPRUCE;
        } else if(wood.is(BlockTags.BIRCH_LOGS)) {
            variant = CreakingVariant.BIRCH;
        } else if(wood.is(BlockTags.JUNGLE_LOGS)) {
            variant = CreakingVariant.JUNGLE;
        } else if(wood.is(BlockTags.ACACIA_LOGS)) {
            variant = CreakingVariant.ACACIA;
        } else if(wood.is(BlockTags.DARK_OAK_LOGS)) {
            variant = CreakingVariant.DARK_OAK;
        } else if(wood.is(BlockTags.MANGROVE_LOGS)) {
            variant = CreakingVariant.MANGROVE;
        } else if(wood.is(BlockTags.CHERRY_LOGS)) {
            variant = CreakingVariant.CHERRY;
        }else if(wood.is(BlockTags.CRIMSON_STEMS)) {
            variant = CreakingVariant.CRIMSON;
        } else if(wood.is(BlockTags.WARPED_STEMS)) {
            variant = CreakingVariant.WARPED;
        } else if(wood.is(BlockTags.BAMBOO_BLOCKS)) {
            variant = CreakingVariant.BAMBOO;
        } else if(wood.is(Blocks.MUSHROOM_STEM)) {
            variant = CreakingVariant.MUSHROOM;
        }

        return variant;
    }

}
