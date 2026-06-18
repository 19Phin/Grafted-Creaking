package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.CreakingVariant;
import net.dialingspoon.grafted_creaking.PlatformSpecific;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Creaking.class)
public abstract class CreakingMixin extends Monster implements CreakingInterface {
    @Shadow
    public abstract BlockPos getHomePos();

    @Shadow public abstract boolean isActive();

    @Shadow public abstract void deactivate();

    @Shadow public abstract void setIsActive(boolean bl);

    @Shadow @Final private static EntityDataAccessor<Boolean> CAN_MOVE;

    @Shadow public abstract @Nullable LivingEntity getTarget();

    @Shadow public abstract void tick();

    protected CreakingMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/creaking/CreakingAi;updateActivity(Lnet/minecraft/world/entity/monster/creaking/Creaking;)V"))
    public void makeBlind(ServerLevel serverLevel, CallbackInfo ci) {
        if(grafted_creaking$hasVariant(CreakingVariant.BIRCH)) {
            MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.BLINDNESS, 120, 0, false, false);
            MobEffectUtil.addEffectToPlayersAround(serverLevel, this, this.position(), 10, mobEffectInstance, 200);
        }
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

    @Inject(method = "tick", at = @At("TAIL"))
    protected void floatMangrove(CallbackInfo ci) {
        if (this.grafted_creaking$hasVariant(CreakingVariant.MANGROVE) && this.isInWater()) {
            CollisionContext collisionContext = CollisionContext.of(this);
            if (collisionContext.isAbove(Block.column(16.0, 0.0, 8.0), this.blockPosition(), true)
                    && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void defaultPale(CallbackInfo ci) {
        Creaking creakingEntity = (Creaking) (Object) this;
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.PALE_OAK, false);
        PlatformSpecific.setVariant(creakingEntity, CreakingVariant.PALE_OAK, true);
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/CreakingHeartBlockEntity;creakingHurt()V"))
    protected void onBambooHurt(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (this.grafted_creaking$hasVariant(CreakingVariant.BAMBOO)) {
            BlockPos blockPos = this.getHomePos();
            Optional<Creaking> optional = SpawnUtil.trySpawnMob(
                    EntityTypes.CREAKING, EntitySpawnReason.REINFORCEMENT, serverLevel, blockPos, 5, 16, 8, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER_NO_LEAVES, true
            );
            Creaking creaking = optional.get();
            serverLevel.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position());
            serverLevel.broadcastEntityEvent(creaking, (byte)60);
            Creaking creakingEntity = (Creaking) (Object) this;
            PlatformSpecific.setVariant(creaking, CreakingVariant.getById(PlatformSpecific.getVariant(creakingEntity, false)), false);
            PlatformSpecific.setVariant(creaking, CreakingVariant.getById(PlatformSpecific.getVariant(creakingEntity, true)), true);
        }
    }

    @Override
    public boolean isPreventingPlayerRest(ServerLevel serverLevel, Player player) {
        return super.isPreventingPlayerRest(serverLevel, player) && !grafted_creaking$hasVariant(CreakingVariant.CHERRY);
    }

    @Unique
    public CreakingVariant grafted_creaking$getVariantFromBlock(BlockState wood) {
        CreakingVariant variant = CreakingVariant.PALE_OAK;

        if(wood.is(BlockItemTags.OAK_LOGS.block())) {
            variant = CreakingVariant.OAK;
        } else if(wood.is(BlockItemTags.SPRUCE_LOGS.block())) {
            variant = CreakingVariant.SPRUCE;
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0F);
        } else if(wood.is(BlockItemTags.BIRCH_LOGS.block())) {
            variant = CreakingVariant.BIRCH;
        } else if(wood.is(BlockItemTags.JUNGLE_LOGS.block())) {
            variant = CreakingVariant.JUNGLE;
        } else if(wood.is(BlockItemTags.ACACIA_LOGS.block())) {
            variant = CreakingVariant.ACACIA;
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(.55F);
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

    @Override
    public boolean grafted_creaking$hasVariant(CreakingVariant variant) {
        int id = variant.getId();
        Creaking creakingEntity = (Creaking) (Object) this;
        return id == PlatformSpecific.getVariant(creakingEntity, false)
                || id == PlatformSpecific.getVariant(creakingEntity, true);
    }

    /**
     * @author DialingSpoon
     * @reason Replacing the whole thing for multiple entity type support.
     */
    @Overwrite
    public boolean checkCanMove() {
        List<LivingEntity> list = this.brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(List.of())
                .stream()
                .filter(this::grafted_creaking$shouldAttack)
                .toList();
        boolean bl = this.isActive();
        if (list.isEmpty()) {
            if (bl) {
                this.deactivate();
            }

            return true;
        } else {
            boolean warped = this.grafted_creaking$hasVariant(CreakingVariant.WARPED);
            boolean bl2 = false;

            for (LivingEntity entity : list) {
                if (this.canAttack(entity) && !this.isAlliedTo(entity)) {
                    bl2 = true;
                    boolean pumpkin = grafted_creaking$wearingDisguiseItem(entity);
                    boolean looking = this.grafted_creaking$hasVariant(CreakingVariant.JUNGLE) && isActive() ? this.isLookingAtMe(entity, 0.025, true, false, this.getEyeY()) : this.isLookingAtMe(entity, 0.5, false, true, this.getEyeY(), this.getY() + 0.5 * (double)this.getScale(), (this.getEyeY() + this.getY()) / 2.0);
                    boolean canSeeMe = looking && (!this.grafted_creaking$hasVariant(CreakingVariant.BIRCH)
                            || !entity.hasEffect(MobEffects.BLINDNESS)
                            || entity.distanceToSqr(this) <= 20);
                    if ((!bl || !pumpkin) && canSeeMe) {

                        Optional<LivingEntity> oldAnger = this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
                        if (oldAnger.isPresent() && entity == oldAnger.get()) {
                            return warped;
                        }

                        if (entity.distanceToSqr(this) < 144f) {
                            this.grafted_creaking$activate(entity);
                            return warped;
                        }
                    }
                }
            }

            if (!bl2 && bl) {
                this.deactivate();
            }

            return !warped;
        }
    }


    @Unique
    private static boolean grafted_creaking$wearingDisguiseItem(LivingEntity livingEntity) {
        ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        return itemStack.is(ItemTags.GAZE_DISGUISE_EQUIPMENT);
    }

    @Unique
    public void grafted_creaking$activate(LivingEntity entity) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, entity);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(SoundEvents.CREAKING_ACTIVATE);
        this.setIsActive(true);
    }

    @Unique
    public boolean grafted_creaking$shouldAttack(LivingEntity entity) {
        if (!entity.canBeSeenAsEnemy() || entity instanceof Creaking) {
            return false;
        }

        if (entity instanceof Player) {
            return !this.grafted_creaking$hasVariant(CreakingVariant.CHERRY);
        } else if (entity instanceof Enemy && !(entity instanceof Creeper)) {
            return this.grafted_creaking$hasVariant(CreakingVariant.MUSHROOM) || this.grafted_creaking$hasVariant(CreakingVariant.CRIMSON);
        } else return this.grafted_creaking$hasVariant(CreakingVariant.CRIMSON);
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return this.entityData.get(CAN_MOVE) || grafted_creaking$hasVariant(CreakingVariant.WARPED) || grafted_creaking$hasVariant(CreakingVariant.JUNGLE);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.WATER) && grafted_creaking$hasVariant(CreakingVariant.MANGROVE);
    }

}
