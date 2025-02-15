package by.vladzuev.locationreceiver.protocol.core.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

public final class TextProtocolDecoder extends ProtocolDecoder<String> {

    public TextProtocolDecoder(final List<? extends PackageDecoder<String>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected String createSource(final ByteBuf buffer) {
        return buffer.toString(US_ASCII);
    }
}
