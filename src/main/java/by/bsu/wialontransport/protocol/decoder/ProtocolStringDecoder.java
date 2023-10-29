package by.bsu.wialontransport.protocol.decoder;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class ProtocolStringDecoder<P extends Package, D extends PackageStringDecoder<P>>
        extends ProtocolDecoder<String, P, D> {
    private static final Charset CHARSET_TO_DECODE_BUFFER = UTF_8;

    public ProtocolStringDecoder(final List<D> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    public final String createSource(final ByteBuf byteBuf) {
        return byteBuf.toString(CHARSET_TO_DECODE_BUFFER);
    }
}
