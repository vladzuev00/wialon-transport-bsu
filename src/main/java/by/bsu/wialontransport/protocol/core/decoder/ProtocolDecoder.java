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
    protected final void decode(final ChannelHandlerContext context,
                                final ByteBuf buffer,
                                final List<Object> outObjects) {
        buffer.retain();
        try {
            final SOURCE source = createSource(buffer);
            final PackageDecoder<PREFIX, SOURCE, ?> decoder = findPackageDecoder(source);
            final Package requestPackage = decoder.decode(source);
            outObjects.add(requestPackage);
        } finally {
            buffer.release();
        }
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    protected abstract PREFIX extractPackagePrefix(final SOURCE source);

    private PackageDecoder<PREFIX, SOURCE, ?> findPackageDecoder(final SOURCE source) {
        final PREFIX packagePrefix = extractPackagePrefix(source);
        return packageDecoders.stream()
                .filter(packageDecoder -> packageDecoder.isAbleToDecode(packagePrefix))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageDecoderException(
                                "No package decoder for source: %s".formatted(source)
                        )
                );
    }

    static final class NoSuitablePackageDecoderException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoSuitablePackageDecoderException() {

        }

        public NoSuitablePackageDecoderException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageDecoderException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageDecoderException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
