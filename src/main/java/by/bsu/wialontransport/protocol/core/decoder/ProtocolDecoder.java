package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<PREFIX, SOURCE> extends ByteToMessageDecoder {
    private final List<? extends PackageDecoder<PREFIX, SOURCE, ?>> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context, final ByteBuf buffer, final List<Object> out) {
        buffer.retain();
        try {
            final SOURCE source = createSource(buffer);
            final PackageDecoder<PREFIX, SOURCE, ?> decoder = findPackageDecoder(source);
            final Package request = decoder.decode(source);
            out.add(request);
        } finally {
            buffer.release();
        }
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    protected abstract PREFIX getPrefix(final SOURCE source);

    private PackageDecoder<PREFIX, SOURCE, ?> findPackageDecoder(final SOURCE source) {
        final PREFIX prefix = getPrefix(source);
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleToDecode(prefix))
                .findFirst()
                .orElseThrow(() -> createNoPackageDecoderException(source));
    }

    private NoPackageDecoderException createNoPackageDecoderException(final SOURCE source) {
        return new NoPackageDecoderException("No package decoder for source: %s".formatted(source));
    }

    static final class NoPackageDecoderException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoPackageDecoderException() {

        }

        public NoPackageDecoderException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoPackageDecoderException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoPackageDecoderException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
