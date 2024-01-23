package by.bsu.wialontransport.protocol.core.server;

import by.bsu.wialontransport.configuration.property.ProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public final class ProtocolServerTest {
    private static final String FIELD_NAME_INET_SOCKET_ADDRESS = "inetSocketAddress";
    private static final String FIELD_NAME_CONTEXT_ATTRIBUTE_MANAGER = "contextAttributeManager";
    private static final String FIELD_NAME_LOOP_GROUP_PROCESSING_CONNECTION = "loopGroupProcessingConnection";
    private static final String FIELD_NAME_LOOP_GROUP_PROCESSING_DATA = "loopGroupProcessingData";
    private static final String FIELD_NAME_CONNECTION_FILE_TIMEOUT_SECONDS = "connectionLifeTimeoutSeconds";

    private static final String GIVEN_HOST = "localhost";
    private static final int GIVEN_PORT = 4004;
    private static final int GIVEN_THREAD_COUNT_PROCESSING_CONNECTION = 5;
    private static final int GIVEN_THREAD_COUNT_PROCESSING_DATA = 10;
    private static final int GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS = 20;
    private static final ProtocolServerConfiguration GIVEN_CONFIGURATION = createServerConfiguration();

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private ProtocolDecoder<?, ?> mockedProtocolDecoder;

    @Mock
    private ProtocolEncoder mockedProtocolEncoder;

    @Mock
    private ProtocolHandler mockedProtocolHandler;

    private ProtocolServer server;

    @Before
    public void initializeServer() {
        server = new TestProtocolServer(
                mockedContextAttributeManager,
                GIVEN_CONFIGURATION,
                mockedProtocolDecoder,
                mockedProtocolEncoder,
                mockedProtocolHandler
        );
    }

    @Test
    public void serverShouldBeCreated() {
        final ContextAttributeManager actualContextAttributeManager = findContextAttributeManager(server);
        assertSame(mockedContextAttributeManager, actualContextAttributeManager);

        final InetSocketAddress actualInetSocketAddress = findInetSocketAddress(server);
        final InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(GIVEN_HOST, GIVEN_PORT);
        assertEquals(expectedInetSocketAddress, actualInetSocketAddress);

        final EventLoopGroup actualLoopGroupProcessingConnection = findLoopGroupProcessingConnection(server);
        assertTrue(actualLoopGroupProcessingConnection instanceof NioEventLoopGroup);
        final int actualLoopGroupProcessingConnectionExecutorCount = findExecutorCount(
                actualLoopGroupProcessingConnection
        );
        assertEquals(GIVEN_THREAD_COUNT_PROCESSING_CONNECTION, actualLoopGroupProcessingConnectionExecutorCount);

        final EventLoopGroup actualLoopGroupProcessingData = findLoopGroupProcessingData(server);
        assertTrue(actualLoopGroupProcessingData instanceof NioEventLoopGroup);
        final int actualLoopGroupProcessingDataExecutorCount = findExecutorCount(actualLoopGroupProcessingData);
        assertEquals(GIVEN_THREAD_COUNT_PROCESSING_DATA, actualLoopGroupProcessingDataExecutorCount);

        final int actualConnectionLifeTimeoutSeconds = findConnectionLifeTimeoutSeconds(server);
        assertEquals(GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS, actualConnectionLifeTimeoutSeconds);
    }

    @Test
    public void serverShouldBeRun()
            throws Exception {
        new Thread(server::run).start();
        SECONDS.sleep(1);

        assertTrue(isServerRun());
    }

    @Test
    public void serverShouldBeStoppedBecauseOfInterrupting() {
        final Thread serverThread = new Thread(server::run);
        serverThread.start();
        serverThread.interrupt();
        assertFalse(isServerRun());
    }

    @Test
    public void serverShouldBeStopped()
            throws Exception {
        final Thread runningServerThread = new Thread(server::run);
        runningServerThread.start();
        SECONDS.sleep(1);

        server.stop();
        runningServerThread.join();

        assertFalse(isServerRun());
    }

    private static ProtocolServerConfiguration createServerConfiguration() {
        return new ProtocolServerConfiguration(
                GIVEN_HOST,
                GIVEN_PORT,
                GIVEN_THREAD_COUNT_PROCESSING_CONNECTION,
                GIVEN_THREAD_COUNT_PROCESSING_DATA,
                GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS
        ) {
        };
    }

    private static InetSocketAddress findInetSocketAddress(final ProtocolServer server) {
        return findProperty(server, FIELD_NAME_INET_SOCKET_ADDRESS, InetSocketAddress.class);
    }

    private static ContextAttributeManager findContextAttributeManager(final ProtocolServer server) {
        return findProperty(server, FIELD_NAME_CONTEXT_ATTRIBUTE_MANAGER, ContextAttributeManager.class);
    }

    private static EventLoopGroup findLoopGroupProcessingConnection(final ProtocolServer server) {
        return findProperty(server, FIELD_NAME_LOOP_GROUP_PROCESSING_CONNECTION, EventLoopGroup.class);
    }

    private static EventLoopGroup findLoopGroupProcessingData(final ProtocolServer server) {
        return findProperty(server, FIELD_NAME_LOOP_GROUP_PROCESSING_DATA, EventLoopGroup.class);
    }

    private static Integer findConnectionLifeTimeoutSeconds(final ProtocolServer server) {
        return findProperty(server, FIELD_NAME_CONNECTION_FILE_TIMEOUT_SECONDS, Integer.class);
    }

    private static int findExecutorCount(final EventLoopGroup eventLoopGroup) {
        return ((NioEventLoopGroup) eventLoopGroup).executorCount();
    }

    private static boolean isServerRun() {
        try (final Socket ignored = new Socket(GIVEN_HOST, GIVEN_PORT)) {
            return true;
        } catch (final IOException exception) {
            return false;
        }
    }

    private static final class TestProtocolServer extends ProtocolServer {
        private final ProtocolDecoder<?, ?> protocolDecoder;
        private final ProtocolEncoder protocolEncoder;
        private final ProtocolHandler protocolHandler;

        public TestProtocolServer(final ContextAttributeManager contextAttributeManager,
                                  final ProtocolServerConfiguration configuration,
                                  final ProtocolDecoder<?, ?> protocolDecoder,
                                  final ProtocolEncoder protocolEncoder,
                                  final ProtocolHandler protocolHandler) {
            super(contextAttributeManager, configuration);
            this.protocolDecoder = protocolDecoder;
            this.protocolEncoder = protocolEncoder;
            this.protocolHandler = protocolHandler;
        }

        @Override
        protected ProtocolDecoder<?, ?> createDecoder() {
            return protocolDecoder;
        }

        @Override
        protected ProtocolEncoder createEncoder() {
            return protocolEncoder;
        }

        @Override
        protected ProtocolHandler createHandler() {
            return protocolHandler;
        }
    }
}
