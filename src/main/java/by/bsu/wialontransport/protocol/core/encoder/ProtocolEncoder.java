package by.bsu.wialontransport.protocol.core.encoder;

import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public abstract class ProtocolEncoder extends MessageToByteEncoder<Package> {
    private static final String TEMPLATE_ENCODING_RESPONSE_MESSAGE = "Response was encoded to '{}'";
    private static final Charset ENCODED_RESPONSE_CHARSET = UTF_8;

    private final List<? extends PackageEncoder<?>> packageEncoders;


    @Override
    protected final void encode(final ChannelHandlerContext context, final Package response, final ByteBuf outBuffer) {
        final PackageEncoder<?> packageEncoder = findPackageEncoder(response);
        final String encodedResponse = packageEncoder.encode(response);
        outBuffer.writeCharSequence(encodedResponse, ENCODED_RESPONSE_CHARSET);
        log.info(TEMPLATE_ENCODING_RESPONSE_MESSAGE, encodedResponse);
    }

    private PackageEncoder<?> findPackageEncoder(final Package response) {
        return packageEncoders.stream()
                .filter(encoder -> encoder.isAbleToEncode(response))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageEncoderException(
                                "No package encoder for response: %s".formatted(response)
                        )
                );
    }

    static final class NoSuitablePackageEncoderException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoSuitablePackageEncoderException() {

        }

        public NoSuitablePackageEncoderException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageEncoderException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageEncoderException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
