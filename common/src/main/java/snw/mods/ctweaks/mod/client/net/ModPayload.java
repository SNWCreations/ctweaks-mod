package snw.mods.ctweaks.mod.client.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import snw.mods.ctweaks.ModConstants;

public record ModPayload(ByteBuf data) implements CustomPacketPayload {
    public static final Type<ModPayload> TYPE;
    public static final StreamCodec<? super RegistryFriendlyByteBuf, ModPayload> CODEC;

    static {
        TYPE = new Type<>(ResourceLocation.parse(ModConstants.CHANNEL));
        CODEC = CustomPacketPayload.codec((payload, buf) -> {
            buf.writeBytes(payload.data);
        }, (buf) -> {
            final int i = buf.readableBytes();
            final ByteBuf raw = buf.readBytes(i);
            return new ModPayload(raw);
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
