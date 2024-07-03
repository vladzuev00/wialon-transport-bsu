package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.netty.buffer.ByteBufUtil.hexDump;

@RequiredArgsConstructor
public final class ProtocolDecoder extends ByteToMessageDecoder {
    private final List<PackageDecoder> packageDecoders;

    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf buffer, final List<Object> out) {
        buffer.retain();
        try {
            final Package request = decode(buffer);
            out.add(request);
        } finally {
            buffer.release();
        }
    }

    private Package decode(final ByteBuf buffer) {
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleToDecode(buffer))
                .findFirst()
                .orElseThrow(() -> createNoPackageDecoderException(buffer))
                .decode(buffer);
    }

    private IllegalStateException createNoPackageDecoderException(final ByteBuf buffer) {
        return new IllegalStateException("No package decoder for '%s'".formatted(hexDump(buffer)));
    }
}
