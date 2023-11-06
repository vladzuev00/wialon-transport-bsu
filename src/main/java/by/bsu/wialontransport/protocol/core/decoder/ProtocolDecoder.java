package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<PREFIX, SOURCE, DECODER extends PackageDecoder<PREFIX, SOURCE, ?>>
        extends ReplayingDecoder<Package> {
    private final List<DECODER> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context,
                                final ByteBuf buffer,
                                final List<Object> outObjects) {
        final SOURCE source = this.createSource(buffer);
        final DECODER decoder = this.findPackageDecoder(source);
        final Package requestPackage = decoder.decode(source);
        outObjects.add(requestPackage);
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    protected abstract PREFIX extractPackagePrefix(final SOURCE source);

    private DECODER findPackageDecoder(final SOURCE source) {
        final PREFIX packagePrefix = this.extractPackagePrefix(source);
        return this.packageDecoders.stream()
                .filter(packageDecoder -> packageDecoder.isAbleToDecode(packagePrefix))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageDecoderException(
                                "No package decoder for source: %s".formatted(source)
                        )
                );
    }

    private static final class NoSuitablePackageDecoderException extends RuntimeException {

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
