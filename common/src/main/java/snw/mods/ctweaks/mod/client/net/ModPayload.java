package snw.mods.ctweaks.mod.client.net;

import io.netty.buffer.ByteBufUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import snw.mods.ctweaks.ModConstants;

public record ModPayload(byte[] data) implements CustomPacketPayload {
    public static final Type<ModPayload> TYPE;
    public static final StreamCodec<? super RegistryFriendlyByteBuf, ModPayload> CODEC;

    static {
        TYPE = new Type<>(ResourceLocation.parse(ModConstants.CHANNEL));
        CODEC = CustomPacketPayload.codec(ModPayload::write, ModPayload::new);
    }

    private ModPayload(FriendlyByteBuf friendlyByteBuf) {
        this(ByteBufUtil.getBytes(friendlyByteBuf));
        friendlyByteBuf.readerIndex(friendlyByteBuf.readerIndex() + this.data.length);
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBytes(this.data);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
