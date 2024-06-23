package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<PREFIX> extends ByteToMessageDecoder {
    private final List<? extends PackageDecoder<PREFIX, SOURCE>> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context, final ByteBuf buffer, final List<Object> out) {
        buffer.retain();
        try {
            decode(buffer, out);
        } finally {
            buffer.release();
        }
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    protected abstract PREFIX getPrefix(final SOURCE source);

    private void decode(final ByteBuf buffer, final List<Object> out) {
        final SOURCE source = createSource(buffer);
        final PackageDecoder<PREFIX, SOURCE> decoder = findDecoder(source);
        final Package request = decoder.decode(source);
        out.add(request);
    }

    private PackageDecoder<PREFIX, SOURCE> findDecoder(final SOURCE source) {
        final PREFIX prefix = getPrefix(source);
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleToDecode(prefix))
                .findFirst()
                .orElseThrow(() -> createNoDecoderException(source));
    }

    private NoDecoderException createNoDecoderException(final SOURCE source) {
        return new NoDecoderException("No decoder for source: %s".formatted(source));
    }

    static final class NoDecoderException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoDecoderException() {

        }

        public NoDecoderException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoDecoderException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoDecoderException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
