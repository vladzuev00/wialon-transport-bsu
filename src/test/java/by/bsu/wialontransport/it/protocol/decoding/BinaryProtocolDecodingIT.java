package by.bsu.wialontransport.it.protocol.decoding;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;

public abstract class BinaryProtocolDecodingIT extends ProtocolDecodingIT {

    public BinaryProtocolDecodingIT(final ProtocolDecoder<?> decoder) {
        super(decoder);
    }

    @Override
    protected final byte[] getBytes(final String source) {
        return decodeHexDump(source);
    }
}
