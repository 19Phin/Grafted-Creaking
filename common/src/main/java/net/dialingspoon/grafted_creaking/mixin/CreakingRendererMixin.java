package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.GraftedCreaking;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingInterface;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingRenderStateInterface;
import net.dialingspoon.grafted_creaking.CreakingSecondLayer;
import net.minecraft.client.model.CreakingModel;
import net.minecraft.client.renderer.entity.CreakingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CreakingRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.creaking.Creaking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingRenderer.class)
public class CreakingRendererMixin<T extends Creaking> extends MobRenderer<T, CreakingRenderState, CreakingModel>{

    private static final ResourceLocation OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/oak.png");
    private static final ResourceLocation SPRUCE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/spruce.png");
    private static final ResourceLocation BIRCH = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/birch.png");
    private static final ResourceLocation JUNGLE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/jungle.png");
    private static final ResourceLocation ACACIA = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/acacia.png");
    private static final ResourceLocation DARK_OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/dark_oak.png");
    private static final ResourceLocation MANGROVE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/mangrove.png");
    private static final ResourceLocation CHERRY = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/cherry.png");
    private static final ResourceLocation PALE_OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/pale_oak.png");
    private static final ResourceLocation CRIMSON = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/crimson/crimson.png");
    private static final ResourceLocation WARPED = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/warped/warped.png");
    private static final ResourceLocation BAMBOO = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/bamboo.png");
    private static final ResourceLocation MUSHROOM = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/mushroom.png");

    public CreakingRendererMixin(EntityRendererProvider.Context context, CreakingModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/CreakingRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
    public void addWoodLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new CreakingSecondLayer((CreakingRenderer)(Object)this, context.getModelSet()));
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/CreakingRenderState;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getCreakingTexture (CreakingRenderState creakingRenderState, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation customTexture = getCustomTextureForVariant(
                ((CreakingRenderStateInterface) creakingRenderState).grafted_creaking$getVariant(false)
        );
        cir.setReturnValue(customTexture);
    }


    private ResourceLocation getCustomTextureForVariant(int variant) {
        ResourceLocation texture;
        texture = switch (variant) {
            case 0 -> OAK;
            case 1 -> SPRUCE;
            case 2 -> BIRCH;
            case 3 -> JUNGLE;
            case 4 -> ACACIA;
            case 5 -> DARK_OAK;
            case 6 -> MANGROVE;
            case 7 -> CHERRY;
            default -> PALE_OAK;
            case 9 -> CRIMSON;
            case 10 -> WARPED;
            case 11 -> BAMBOO;
            case 12 -> MUSHROOM;
        };

        return texture;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/creaking/Creaking;Lnet/minecraft/client/renderer/entity/state/CreakingRenderState;F)V", at = @At("TAIL"))
    public void extractVariant(T creakingEntity, CreakingRenderState creakingRenderState, float f, CallbackInfo ci) {
        CreakingRenderStateInterface renderState = (CreakingRenderStateInterface) creakingRenderState;
        CreakingInterface creaking = (CreakingInterface) creakingEntity;
        renderState.grafted_creaking$setVariant(creaking.grafted_creaking$getVariant(false), false);
        renderState.grafted_creaking$setVariant(creaking.grafted_creaking$getVariant(true), true);
        renderState.grafted_creaking$setAge(creakingEntity.tickCount);
    }

    @Shadow
    public ResourceLocation getTextureLocation(CreakingRenderState livingEntityRenderState) {
        return null;
    }

    @Shadow
    public CreakingRenderState createRenderState() {
        return null;
    }
}
