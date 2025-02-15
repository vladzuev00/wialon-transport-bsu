package by.vladzuev.locationreceiver.it.protocol.decoding.binary;

import by.vladzuev.locationreceiver.it.protocol.decoding.ProtocolDecodingIT;
import by.vladzuev.locationreceiver.protocol.core.decoder.BinaryProtocolDecoder;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;

public abstract class BinaryProtocolDecodingIT extends ProtocolDecodingIT {

    public BinaryProtocolDecodingIT(final BinaryProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected final byte[] getBytes(final String source) {
        return decodeHexDump(source);
    }
}
