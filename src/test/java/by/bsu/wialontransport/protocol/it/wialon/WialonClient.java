package by.bsu.wialontransport.protocol.it.wialon;

import by.bsu.wialontransport.protocol.it.core.client.TextClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public final class WialonClient extends TextClient {
    private static final char PACKAGE_LAST_CHARACTER = '\n';

    public WialonClient(final InetSocketAddress address)
            throws IOException {
        super(address);
    }

    @Override
    protected byte[] receiveResponseBytes(final InputStream inputStream)
            throws IOException {
        final List<Integer> receivedBytes = new ArrayList<>();
        int receivedByte;
        do {
            receivedByte = inputStream.read();
            receivedBytes.add(receivedByte);
        } while (receivedByte != PACKAGE_LAST_CHARACTER);
        return mapToByteArray(receivedBytes);
    }

    private static byte[] mapToByteArray(final List<Integer> values) {
        final byte[] result = new byte[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i).byteValue();
        }
        return result;
    }
}
