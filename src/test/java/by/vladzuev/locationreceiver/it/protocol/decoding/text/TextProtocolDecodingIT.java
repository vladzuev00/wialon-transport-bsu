package by.vladzuev.locationreceiver.it.protocol.decoding.text;

import by.vladzuev.locationreceiver.it.protocol.decoding.ProtocolDecodingIT;
import by.vladzuev.locationreceiver.protocol.core.decoder.TextProtocolDecoder;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class TextProtocolDecodingIT extends ProtocolDecodingIT {

    public TextProtocolDecodingIT(final TextProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected final byte[] getBytes(final String source) {
        return source.getBytes(US_ASCII);
    }
}
