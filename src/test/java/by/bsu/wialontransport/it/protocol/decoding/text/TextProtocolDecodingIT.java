package by.bsu.wialontransport.it.protocol.decoding.text;

import by.bsu.wialontransport.it.protocol.decoding.ProtocolDecodingIT;
import by.bsu.wialontransport.protocol.core.decoder.TextProtocolDecoder;

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
