package net.dialingspoon.grafted_creaking.mixin;

import net.dialingspoon.grafted_creaking.Interfaces.CreakingRenderStateInterface;
import net.minecraft.client.renderer.entity.state.CreakingRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreakingRenderState.class)
public class CreakingRenderStateMixin implements CreakingRenderStateInterface {
    @Unique
    private int grafted_creaking$variant;
    @Unique
    private int grafted_creaking$variant2;
    @Unique
    private int grafted_creaking$age;

    @Override
    public int grafted_creaking$getVariant(boolean second) {
        return second ? grafted_creaking$variant2 : grafted_creaking$variant;
    }

    @Override
    public void grafted_creaking$setVariant(int grafted_creaking$variant, boolean second) {
        if (second) this.grafted_creaking$variant2 = grafted_creaking$variant;
        else this.grafted_creaking$variant = grafted_creaking$variant;
    }

    @Override
    public int grafted_creaking$getAge() {
        return grafted_creaking$age;
    }

    @Override
    public void grafted_creaking$setAge(int grafted_creaking$age) {
        this.grafted_creaking$age = grafted_creaking$age;
    }
}
