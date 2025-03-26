package net.dialingspoon.grafted_creaking.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreakingHeartBlock.class)
public class CreakingHeartBlockMixin {

    @Shadow @Final public static EnumProperty<CreakingHeartState> STATE;

    @Redirect(method = "hasRequiredLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean isWood(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }

    @WrapOperation(method = "hasRequiredLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", ordinal = 1))
    private static Comparable isMushroom(BlockState instance, Property property, Operation<Comparable> original, @Local Direction.Axis axis) {
        if (instance.is(Blocks.MUSHROOM_STEM)) {
            return axis;
        }
        return original.call(instance, property);
    }

    @Redirect(method = "isSurroundedByLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean isWoodAround(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }

    @Redirect(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CreakingHeartBlock;isNaturalNight(Lnet/minecraft/world/level/Level;)Z"))
    private boolean heartState(Level level, @Local BlockState blockState) {
        return blockState.getValue(STATE) == CreakingHeartState.AWAKE;
    }
}
