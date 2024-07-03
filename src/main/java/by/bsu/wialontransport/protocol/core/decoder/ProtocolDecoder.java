package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<SOURCE> extends ByteToMessageDecoder {
    private final List<PackageDecoder<SOURCE>> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context, final ByteBuf buffer, final List<Object> out) {
        buffer.retain();
        try {
            final SOURCE source = createSource(buffer);
            final Package request = decode(source);
            out.add(request);
        } finally {
            buffer.release();
        }
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    private Package decode(final SOURCE source) {
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleToDecode(source))
                .findFirst()
                .orElseThrow(() -> createNoPackageDecoderException(source))
                .decode(source);
    }

    private IllegalStateException createNoPackageDecoderException(final SOURCE source) {
        return new IllegalStateException("No package decoder for '%s'".formatted(source));
    }
}
