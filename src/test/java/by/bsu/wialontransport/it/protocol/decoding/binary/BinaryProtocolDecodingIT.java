package by.bsu.wialontransport.it.protocol.decoding.binary;

import by.bsu.wialontransport.it.protocol.decoding.ProtocolDecodingIT;
import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;

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
