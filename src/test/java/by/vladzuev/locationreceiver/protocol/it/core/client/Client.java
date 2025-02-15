package by.vladzuev.locationreceiver.protocol.it.core.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class Client<REQUEST> implements AutoCloseable {
    private static final Charset RESPONSE_CHARSET = UTF_8;

    private final Socket socket;

    public Client(final InetSocketAddress address)
            throws IOException {
        socket = new Socket(address.getAddress(), address.getPort());
    }

    public final String request(final REQUEST request)
            throws IOException {
        send(getBytes(request));
        return receiveResponse();
    }

    public final void requestExpectingResponse(final REQUEST request,
                                               final String expectedResponse,
                                               final String notExpectedResponseMessage)
            throws IOException {
        final String response = request(request);
        if (!Objects.equals(expectedResponse, response)) {
            throw new IllegalStateException(notExpectedResponseMessage);
        }
    }

    @Override
    public final void close()
            throws IOException {
        socket.close();
    }

    protected abstract byte[] getBytes(final REQUEST request);

    protected abstract byte[] receiveResponseBytes(final InputStream inputStream) throws IOException;

    private void send(final byte[] bytes)
            throws IOException {
        socket.getOutputStream().write(bytes);
    }

    private String receiveResponse()
            throws IOException {
        final byte[] bytes = receiveResponseBytes(socket.getInputStream());
        return new String(bytes, RESPONSE_CHARSET);
    }
}
