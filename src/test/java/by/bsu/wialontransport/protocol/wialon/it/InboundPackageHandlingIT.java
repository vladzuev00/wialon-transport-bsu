package by.bsu.wialontransport.protocol.wialon.it;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.configuration.WialonServerConfiguration;
import by.bsu.wialontransport.protocol.wialon.server.WialonServer;
import by.bsu.wialontransport.protocol.wialon.server.factory.WialonServerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public final class InboundPackageHandlingIT extends AbstractContextTest {
    private static boolean serverWasRan = false;

    @Autowired
    private WialonServerConfiguration serverConfiguration;

    @Autowired
    private WialonServerFactory serverFactory;

    private Client client;

    @Before
    public void startServerAndClient()
            throws Exception {
        if (!serverWasRan) {
            final WialonServer server = this.serverFactory.create();
            new Thread(server::run).start();
            //TODO: correct
            SECONDS.sleep(1);  //to give thread starting server time to run it
            serverWasRan = true;
        }
        this.client = new Client(this.serverConfiguration);
    }

    @After
    public void closeClient()
            throws IOException {
        this.client.close();
    }

    @Test
    public void loginPackageShouldBeHandledWithSuccessAuthorizationStatus()
            throws Exception {
        final String givenRequest = "#L#11112222333344445555;password\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AL#1\r\n";
        assertEquals(expected, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
            throws Exception {
        final String givenRequest = "#L#11112222333344445555;wrong_password\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AL#01\r\n";
        assertEquals(expected, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
            throws Exception {
        final String givenRequest = "#L#00000000000000000000;wrong_password\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AL#0\r\n";
        assertEquals(expected, actual);
    }

    private static final class Client implements AutoCloseable {
        private static final char PACKAGE_LAST_CHARACTER = '\n';

        private final Socket socket;
        private final ExecutorService executorService;

        public Client(final WialonServerConfiguration serverConfiguration)
                throws IOException {
            this.socket = new Socket(serverConfiguration.getHost(), serverConfiguration.getPort());
            this.executorService = newSingleThreadExecutor();
        }

        public Future<String> doRequest(final String request) {
            return this.executorService.submit(() -> {
                final OutputStream outputStream = this.socket.getOutputStream();
                outputStream.write(request.getBytes(UTF_8));

                final InputStream inputStream = this.socket.getInputStream();
                final StringBuilder responseBuilder = new StringBuilder();
                char currentReadChar;
                do {
                    currentReadChar = (char) inputStream.read();
                    responseBuilder.append(currentReadChar);
                } while (currentReadChar != PACKAGE_LAST_CHARACTER);

                return responseBuilder.toString();
            });
        }

        public void doResponse(final String response)
                throws IOException {
            final OutputStream outputStream = this.socket.getOutputStream();
            outputStream.write(response.getBytes(UTF_8));
        }

        @Override
        public void close() throws IOException {
            this.executorService.shutdownNow();
            this.socket.close();
        }
    }
}
