package by.bsu.wialontransport.protocol.it.newwing;

import by.bsu.wialontransport.protocol.it.core.client.BinaryClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

public final class NewWingClient extends BinaryClient {
    private static final int RESPONSE_PACKAGE_LENGTH = 5;

    public NewWingClient(final InetSocketAddress address)
            throws IOException {
        super(address);
    }

    @Override
    protected byte[] receiveResponseBytes(final InputStream inputStream)
            throws IOException {
        final byte[] bytes = new byte[RESPONSE_PACKAGE_LENGTH];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) inputStream.read();
        }
        return bytes;
    }


}
