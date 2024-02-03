package by.bsu.wialontransport.protocol.it.core.client;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class BinaryProtocol extends Client<byte[]> {

    public BinaryProtocol(final InetSocketAddress address)
            throws IOException {
        super(address);
    }

    @Override
    protected final byte[] getBytes(final byte[] bytes) {
        return bytes;
    }
}
