package by.vladzuev.locationreceiver.protocol.core.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ProtocolDecoder<SOURCE> extends ByteToMessageDecoder {
    private final List<? extends PackageDecoder<SOURCE>> packageDecoders;

    @Override
    protected final void decode(final ChannelHandlerContext context, final ByteBuf buffer, final List<Object> out) {
        buffer.retain();
        try {
            final SOURCE source = createSource(buffer);
            final Object request = decodePackage(source);
            out.add(request);
            skipRemaining(buffer);
        } finally {
            buffer.release();
        }
    }

    protected abstract SOURCE createSource(final ByteBuf buffer);

    private Object decodePackage(final SOURCE source) {
        return packageDecoders.stream()
                .filter(decoder -> decoder.isAbleDecode(source))
                .findFirst()
                .orElseThrow(this::createNoPackageDecoderException)
                .decode(source);
    }

    private IllegalStateException createNoPackageDecoderException() {
        return new IllegalStateException("No package decoder");
    }

    private void skipRemaining(final ByteBuf buffer) {
        buffer.skipBytes(buffer.readableBytes());
    }
}
