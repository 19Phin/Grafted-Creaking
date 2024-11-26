package net.dialingspoon.grafted_creaking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dialingspoon.grafted_creaking.Interfaces.CreakingRenderStateInterface;
import net.minecraft.client.model.CreakingModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CreakingRenderState;
import net.minecraft.resources.ResourceLocation;

public class CreakingSecondLayer extends RenderLayer<CreakingRenderState, CreakingModel> {
    private static final ResourceLocation OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/oak_layer.png");
    private static final ResourceLocation SPRUCE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/spruce_layer.png");
    private static final ResourceLocation BIRCH = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/birch_layer.png");
    private static final ResourceLocation JUNGLE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/jungle_layer.png");
    private static final ResourceLocation ACACIA = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/acacia_layer.png");
    private static final ResourceLocation DARK_OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/dark_oak_layer.png");
    private static final ResourceLocation MANGROVE = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/mangrove_layer.png");
    private static final ResourceLocation CHERRY = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/cherry_layer.png");
    private static final ResourceLocation PALE_OAK = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/pale_oak_layer.png");
    private static final ResourceLocation CRIMSON = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/crimson/crimson_layer");
    private static final ResourceLocation WARPED = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/warped/warped_layer");
    private static final ResourceLocation BAMBOO = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/bamboo_layer.png");
    private static final ResourceLocation MUSHROOM = ResourceLocation.tryBuild(GraftedCreaking.MOD_ID, "textures/entity/creaking/mushroom_layer.png");
    private final CreakingModel model;

    public CreakingSecondLayer(RenderLayerParent<CreakingRenderState, CreakingModel> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new CreakingModel(entityModelSet.bakeLayer(ModelLayers.CREAKING));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CreakingRenderState creakingRenderState, float f, float g) {
        CreakingRenderStateInterface stateInterface = (CreakingRenderStateInterface)creakingRenderState;
        int type = stateInterface.grafted_creaking$getVariant(true);

        ResourceLocation resourceLocation = switch (type) {
            case 0 -> OAK;
            case 1 -> SPRUCE;
            case 2 -> BIRCH;
            case 3 -> JUNGLE;
            case 4 -> ACACIA;
            case 5 -> DARK_OAK;
            case 6 -> MANGROVE;
            case 7 -> CHERRY;
            default -> PALE_OAK;
            case 9 -> CRIMSON.withSuffix(((stateInterface.grafted_creaking$getAge() /10) % 5)+1 + ".png");
            case 10 -> WARPED.withSuffix(((stateInterface.grafted_creaking$getAge() /10) % 5)+1 + ".png");
            case 11 -> BAMBOO;
            case 12 -> MUSHROOM;
        };

        if (!creakingRenderState.isInvisible) {
            model.setupAnim(creakingRenderState);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(resourceLocation));
            model.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(creakingRenderState, 0.0F));
        }
    }

}
