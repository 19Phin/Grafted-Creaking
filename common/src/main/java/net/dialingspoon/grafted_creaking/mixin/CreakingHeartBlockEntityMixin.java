package net.dialingspoon.grafted_creaking.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.dialingspoon.grafted_creaking.GraftedCreaking;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingHeartBlockEntityInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.level.block.Blocks;
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

    @WrapOperation(method = {"updateCreakingState", "serverTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;"))
    private static Object heartState(EnvironmentAttributeSystem instance, EnvironmentAttribute environmentAttribute, BlockPos blockPos, Operation<Object> original, @Local CreakingHeartBlockEntity creakingHeartBlockEntity) {
        return GraftedCreaking.shouldSpawn((Boolean) original.call(instance, environmentAttribute, blockPos), creakingHeartBlockEntity);
    }

    @Redirect(method = {"lambda$spreadResin$0", "lambda$spreadResin$1"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean canSpreadResin(BlockState instance, TagKey tagKey) {
        return instance.is(BlockTags.LOGS) || instance.is(Blocks.MUSHROOM_STEM) || instance.is(BlockTags.BAMBOO_BLOCKS);
    }

    public Optional<Creaking> getProtector() {
        return getCreakingProtector();
    }
}
