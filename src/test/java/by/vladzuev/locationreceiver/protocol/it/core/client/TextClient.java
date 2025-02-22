package by.vladzuev.locationreceiver.protocol.it.core.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class TextClient extends Client<String> {
    private static final Charset CHARSET = UTF_8;

    public TextClient(final InetSocketAddress address)
            throws IOException {
        super(address);
    }

    @Override
    protected final byte[] getBytes(final String request) {
        return request.getBytes(CHARSET);
    }
}
