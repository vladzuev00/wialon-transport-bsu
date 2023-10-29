package by.bsu.wialontransport.protocol.decoder;

import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<S, P extends Package, D extends PackageDecoder<S, P>> extends ReplayingDecoder<P> {
    private final List<D> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context,
                                final ByteBuf byteBuf,
                                final List<Object> outObjects) {
        final S source = this.createSource(byteBuf);
        final D decoder = this.findPackageDecoder(source);
        final P requestPackage = decoder.decode(source);
        outObjects.add(requestPackage);
    }

    protected abstract S createSource(final ByteBuf byteBuf);

    private D findPackageDecoder(final S source) {
        return this.packageDecoders.stream()
                .filter(packageDecoder -> packageDecoder.isAbleToDecode(source))
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
