package net.dialingspoon.grafted_creaking.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreakingHeartBlockEntity.class)
public abstract class CreakingHeartBlockEntityMixin {
    @Redirect(method = {"method_65168", "method_65169"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean canSpreadResin(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }
}
