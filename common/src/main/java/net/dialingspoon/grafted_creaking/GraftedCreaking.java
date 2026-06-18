package net.dialingspoon.grafted_creaking;

import net.dialingspoon.grafted_creaking.Interfaces.CreakingHeartBlockEntityInterface;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;

import java.util.Optional;

public final class GraftedCreaking {
    public static final String MOD_ID = "grafted_creaking";

    public static boolean shouldSpawn(boolean isNight, CreakingHeartBlockEntity creakingHeartBlockEntity) {
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
            Level level = creakingHeartBlockEntity.getLevel();
            paleOak = (level.getBlockState(pos.relative(axis.getPositive())).is(BlockTags.PALE_OAK_LOGS) || level.getBlockState(pos.relative(axis.getNegative())).is(BlockTags.PALE_OAK_LOGS));
            darkOak = (level.getBlockState(pos.relative(axis.getPositive())).is(BlockItemTags.DARK_OAK_LOGS.block()) || level.getBlockState(pos.relative(axis.getNegative())).is(BlockItemTags.DARK_OAK_LOGS.block()));
        }

        if (paleOak && darkOak) {
            return false;
        }

        return !paleOak && !darkOak || paleOak == isNight;
    }
}
