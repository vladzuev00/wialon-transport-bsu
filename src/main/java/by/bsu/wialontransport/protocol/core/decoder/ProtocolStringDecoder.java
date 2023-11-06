package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class ProtocolStringDecoder<DECODER extends PackageStringDecoder<?>>
        extends ProtocolDecoder<String, String, DECODER> {
    private static final Charset CHARSET_TO_DECODE_BUFFER = UTF_8;

    public ProtocolStringDecoder(final List<DECODER> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    public final String createSource(final ByteBuf buffer) {
        return buffer.toString(CHARSET_TO_DECODE_BUFFER);
    }
}
