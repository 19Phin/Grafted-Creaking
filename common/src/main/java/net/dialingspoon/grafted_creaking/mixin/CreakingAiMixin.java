package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.creaking.CreakingAi;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CreakingAi.class)
public abstract class CreakingAiMixin {

    @Redirect(method = "initIdleActivity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/StartAttacking;create(Lnet/minecraft/world/entity/ai/behavior/StartAttacking$StartAttackingCondition;Lnet/minecraft/world/entity/ai/behavior/StartAttacking$TargetFinder;)Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;"))
    private static BehaviorControl<Creaking> RedirectBoredAttacks(StartAttacking.StartAttackingCondition<Creaking> arg, StartAttacking.TargetFinder<Creaking> arg2) {
        return StartAttacking.create(
                (serverLevel, creaking) -> creaking.isActive(),
                (serverLevel, creaking) ->
                        creaking.getBrain()
                            .getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(List.of()).stream()
                            .filter(entity -> ((CreakingInterface)creaking).grafted_creaking$shouldAttack(entity)).filter(entity -> !creaking.isAlliedTo(entity))
                            .filter(entity -> Sensor.isEntityTargetable(serverLevel, creaking, entity))
                            .filter(entity -> Sensor.isEntityAttackable(serverLevel, creaking, entity)).findFirst()
        );
    }

}
