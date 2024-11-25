package net.dialingspoon.grafted_creaking.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreakingHeartBlockEntity.class)
public abstract class CreakingHeartBlockEntityMixin {

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CreakingHeartBlock;isNaturalNight(Lnet/minecraft/world/level/Level;)Z"))
    private static boolean shouldSpawn(Level level, @Local CreakingHeartBlockEntity creakingHeartBlockEntity) {
        BlockPos pos = creakingHeartBlockEntity.getBlockPos();
        boolean paleOak = (level.getBlockState(pos.above()).is(BlockTags.PALE_OAK_LOGS) || level.getBlockState(pos.below()).is(BlockTags.PALE_OAK_LOGS));
        boolean darkOak = (level.getBlockState(pos.above()).is(BlockTags.DARK_OAK_LOGS) || level.getBlockState(pos.below()).is(BlockTags.DARK_OAK_LOGS));

        if (paleOak && darkOak) {
            return false;
        }

        return (!paleOak && !darkOak || paleOak == CreakingHeartBlock.isNaturalNight(level));
    }

    @Redirect(method = {"method_65168", "method_65169"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean canSpreadResin(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }
}
