package net.dialingspoon.grafted_creaking.neoforge;

import com.mojang.serialization.Codec;
import net.dialingspoon.grafted_creaking.GraftedCreaking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GraftedCreaking.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> VARIANT = ATTACHMENT_TYPES.register("variant", ModAttachments::createVariantAttachment);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SECOND_VARIANT = ATTACHMENT_TYPES.register("variant_2", ModAttachments::createVariantAttachment);

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

    private static AttachmentType<Integer> createVariantAttachment() {
        return AttachmentType.builder(() -> 8)
                .serialize(Codec.INT.fieldOf("value"), value -> value != 8)
                .sync(ByteBufCodecs.VAR_INT)
                .build();
    }
}
