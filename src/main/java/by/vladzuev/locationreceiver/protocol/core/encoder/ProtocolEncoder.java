package by.vladzuev.locationreceiver.protocol.core.encoder;

import by.vladzuev.locationreceiver.protocol.core.encoder.packages.PackageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class ProtocolEncoder extends MessageToByteEncoder<Object> {
    private final List<? extends PackageEncoder<?>> packageEncoders;

    @Override
    protected void encode(final ChannelHandlerContext context, final Object response, final ByteBuf out) {
        packageEncoders.stream()
                .filter(encoder -> encoder.isAbleEncode(response))
                .findFirst()
                .map(encoder -> encoder.encode(response))
                .ifPresentOrElse(out::writeBytes, this::throwNoPackageEncoderException);
    }

    private void throwNoPackageEncoderException() {
        throw new IllegalStateException("No package encoder");
    }
}
