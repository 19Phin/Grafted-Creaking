package net.dialingspoon.grafted_creaking.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingHeartBlockEntityInterface;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CreakingHeartBlockEntity.class)
public abstract class CreakingHeartBlockEntityMixin implements CreakingHeartBlockEntityInterface {

    @Shadow protected abstract Optional<Creaking> getCreakingProtector();

    @WrapOperation(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CreakingHeartBlock;isNaturalNight(Lnet/minecraft/world/level/Level;)Z"))
    private static boolean shouldDepawn(Level level, Operation<Boolean> original, @Local CreakingHeartBlockEntity creakingHeartBlockEntity) {
        return shouldSpawn(level, original, creakingHeartBlockEntity);
    }

    @WrapOperation(method = "updateCreakingState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CreakingHeartBlock;isNaturalNight(Lnet/minecraft/world/level/Level;)Z"))
    private static boolean heartState(Level level, Operation<Boolean> original, @Local CreakingHeartBlockEntity creakingHeartBlockEntity) {
        return shouldSpawn(level, original, creakingHeartBlockEntity);
    }

    private static boolean shouldSpawn(Level level, Operation<Boolean> original, CreakingHeartBlockEntity creakingHeartBlockEntity) {
        Optional<Creaking> creakingOptional = ((CreakingHeartBlockEntityInterface)creakingHeartBlockEntity).getProtector();
        boolean paleOak;
        boolean darkOak;
        if (creakingOptional.isPresent()) {
            CreakingInterface creaking = ((CreakingInterface)creakingOptional.get());
            paleOak = creaking.grafted_creaking$hasVariant(CreakingVariant.PALE_OAK);
            darkOak = creaking.grafted_creaking$hasVariant(CreakingVariant.DARK_OAK);
        } else {
            BlockPos pos = creakingHeartBlockEntity.getBlockPos();
            Direction.Axis axis = creakingHeartBlockEntity.getBlockState().getValue(CreakingHeartBlock.AXIS);
            paleOak = (level.getBlockState(pos.relative(axis.getPositive())).is(BlockTags.PALE_OAK_LOGS) || level.getBlockState(pos.relative(axis.getNegative())).is(BlockTags.PALE_OAK_LOGS));
            darkOak = (level.getBlockState(pos.relative(axis.getPositive())).is(BlockTags.DARK_OAK_LOGS) || level.getBlockState(pos.relative(axis.getNegative())).is(BlockTags.DARK_OAK_LOGS));
        }

        if (paleOak && darkOak) {
            return false;
        }

        return (!paleOak && !darkOak || paleOak == original.call(level));
    }

    @Redirect(method = {"method_65168", "method_65169"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean canSpreadResin(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }

    public Optional<Creaking> getProtector() {
        return getCreakingProtector();
    }
}
