package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.dialingspoon.grafted_creaking.PlatformSpecific;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creaking.class)
public abstract class CreakingMixin extends Monster {
    @Shadow
    public abstract BlockPos getHomePos();

    protected CreakingMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        Creaking creakingEntity = (Creaking) (Object) this;
        valueOutput.putInt("Variant", PlatformSpecific.getVariant(creakingEntity, false));
        valueOutput.putInt("Variant2", PlatformSpecific.getVariant(creakingEntity, true));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        Creaking creakingEntity = (Creaking) (Object) this;
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.getById(valueInput.getInt("Variant").orElse(8)), false);
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.getById(valueInput.getInt("Variant2").orElse(8)), true);
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

        Creaking creakingEntity = (Creaking) (Object) this;
        PlatformSpecific.setVariant(creakingEntity, variant, false);
        PlatformSpecific.setVariant(creakingEntity, variant2, true);

    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void defaultPale(CallbackInfo ci) {
        Creaking creakingEntity = (Creaking) (Object) this;
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.PALE_OAK, false);
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.PALE_OAK, true);
    }

    @Unique
    public CreakingVariant grafted_creaking$getVariantFromBlock(BlockState wood) {
        CreakingVariant variant = CreakingVariant.PALE_OAK;

        if(wood.is(BlockItemTags.OAK_LOGS.block())) {
            variant = CreakingVariant.OAK;
        } else if(wood.is(BlockItemTags.SPRUCE_LOGS.block())) {
            variant = CreakingVariant.SPRUCE;
        } else if(wood.is(BlockItemTags.BIRCH_LOGS.block())) {
            variant = CreakingVariant.BIRCH;
        } else if(wood.is(BlockItemTags.JUNGLE_LOGS.block())) {
            variant = CreakingVariant.JUNGLE;
        } else if(wood.is(BlockItemTags.ACACIA_LOGS.block())) {
            variant = CreakingVariant.ACACIA;
        } else if(wood.is(BlockItemTags.DARK_OAK_LOGS.block())) {
            variant = CreakingVariant.DARK_OAK;
        } else if(wood.is(BlockItemTags.MANGROVE_LOGS.block())) {
            variant = CreakingVariant.MANGROVE;
        } else if(wood.is(BlockItemTags.CHERRY_LOGS.block())) {
            variant = CreakingVariant.CHERRY;
        }else if(wood.is(BlockItemTags.CRIMSON_STEMS.block())) {
            variant = CreakingVariant.CRIMSON;
        } else if(wood.is(BlockItemTags.WARPED_STEMS.block())) {
            variant = CreakingVariant.WARPED;
        } else if(wood.is(BlockItemTags.BAMBOO_BLOCKS.block())) {
            variant = CreakingVariant.BAMBOO;
        } else if(wood.is(Blocks.MUSHROOM_STEM)) {
            variant = CreakingVariant.MUSHROOM;
        }

        return variant;
    }

}
