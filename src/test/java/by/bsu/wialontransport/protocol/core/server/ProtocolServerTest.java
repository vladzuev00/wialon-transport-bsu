//package by.bsu.wialontransport.protocol.core.server;
//
//import by.bsu.wialontransport.config.property.protocolserver.ProtocolServerConfig;
//import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
//import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
//import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
//import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
//import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
//import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
//import by.bsu.wialontransport.protocol.core.server.ProtocolServer.ServerRunningContext;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.util.List;
//
//import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.junit.Assert.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ProtocolServerTest {
//    private static final String FIELD_NAME_CONFIGURATION = "configuration";
//    private static final String FIELD_NAME_RUNNING_CONTEXT = "runningContext";
//
//    private static final String GIVEN_HOST = "localhost";
//    private static final int GIVEN_PORT = 4004;
//    private static final int GIVEN_THREAD_COUNT_PROCESSING_CONNECTION = 5;
//    private static final int GIVEN_THREAD_COUNT_PROCESSING_DATA = 10;
//    private static final int GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS = 20;
//
//    @Mock
//    private ContextAttributeManager mockedContextAttributeManager;
//
//    @Mock
//    private ConnectionManager mockedConnectionManager;
//
//    @Mock
//    private PackageDecoder<?, ?, ?> firstMockedPackageDecoder;
//
//    @Mock
//    private PackageDecoder<?, ?, ?> secondMockedPackageDecoder;
//
//    @Mock
//    private PackageEncoder<?> firstMockedPackageEncoder;
//
//    @Mock
//    private PackageEncoder<?> secondMockedPackageEncoder;
//
//    @Mock
//    private PackageHandler<?> firstMockedPackageHandler;
//
//    @Mock
//    private PackageHandler<?> secondMockedPackageHandler;
//
//    @Mock
//    private ProtocolDecoder<?, ?> mockedProtocolDecoder;
//
//    @Mock
//    private ProtocolEncoder mockedProtocolEncoder;
//
//    @Mock
//    private ProtocolHandler mockedProtocolHandler;
//
//    private ProtocolServer<PackageDecoder<?, ?, ?>, PackageEncoder<?>> server;
//
//    @Before
//    public void initializeServer() {
//        server = new TestProtocolServer(
//                createServerConfiguration(),
//                mockedContextAttributeManager,
//                mockedConnectionManager,
//                List.of(firstMockedPackageDecoder, secondMockedPackageDecoder),
//                List.of(firstMockedPackageEncoder, secondMockedPackageEncoder),
//                List.of(firstMockedPackageHandler, secondMockedPackageHandler),
//                mockedProtocolDecoder,
//                mockedProtocolEncoder,
//                mockedProtocolHandler
//        );
//    }
//
//    @Test
//    public void serverShouldBeCreated() {
//        final ProtocolServerConfig actualConfiguration = findConfiguration(server);
//
//        final InetSocketAddress actualInetSocketAddress = actualConfiguration.getInetSocketAddress();
//        final InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(GIVEN_HOST, GIVEN_PORT);
//        assertEquals(expectedInetSocketAddress, actualInetSocketAddress);
//
//        final EventLoopGroup actualLoopGroupProcessingConnection = actualConfiguration.getLoopGroupProcessingConnection();
//        assertTrue(actualLoopGroupProcessingConnection instanceof NioEventLoopGroup);
//        final int actualLoopGroupProcessingConnectionExecutorCount = findExecutorCount(
//                actualLoopGroupProcessingConnection
//        );
//        assertEquals(GIVEN_THREAD_COUNT_PROCESSING_CONNECTION, actualLoopGroupProcessingConnectionExecutorCount);
//
//        final EventLoopGroup actualLoopGroupProcessingData = actualConfiguration.getLoopGroupProcessingData();
//        assertTrue(actualLoopGroupProcessingData instanceof NioEventLoopGroup);
//        final int actualLoopGroupProcessingDataExecutorCount = findExecutorCount(actualLoopGroupProcessingData);
//        assertEquals(GIVEN_THREAD_COUNT_PROCESSING_DATA, actualLoopGroupProcessingDataExecutorCount);
//
//        final int actualConnectionLifeTimeoutSeconds = actualConfiguration.getConnectionLifeTimeoutSeconds();
//        assertEquals(GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS, actualConnectionLifeTimeoutSeconds);
//
//        final ServerRunningContext actualRunningContext = findRunningContext(server);
//
//        final ContextAttributeManager actualContextAttributeManager = actualRunningContext
//                .getContextAttributeManager();
//        assertSame(mockedContextAttributeManager, actualContextAttributeManager);
//
//        final ConnectionManager actualConnectionManager = actualRunningContext.getConnectionManager();
//        assertSame(mockedConnectionManager, actualConnectionManager);
//
//        final List<?> actualPackageDecoders = actualRunningContext.getPackageDecoders();
//        final List<PackageDecoder<?, ?, ?>> expectedPackageDecoders = List.of(
//                firstMockedPackageDecoder,
//                secondMockedPackageDecoder
//        );
//        assertEquals(expectedPackageDecoders, actualPackageDecoders);
//
//        final List<?> actualPackageEncoders = actualRunningContext.getPackageEncoders();
//        final List<PackageEncoder<?>> expectedPackageEncoders = List.of(
//                firstMockedPackageEncoder,
//                secondMockedPackageEncoder
//        );
//        assertEquals(expectedPackageEncoders, actualPackageEncoders);
//
//        final List<?> actualPackageHandlers = actualRunningContext.getPackageHandlers();
//        final List<PackageHandler<?>> expectedPackageHandlers = List.of(
//                firstMockedPackageHandler,
//                secondMockedPackageHandler
//        );
//        assertEquals(expectedPackageHandlers, actualPackageHandlers);
//    }
//
//    @Test
//    public void serverShouldBeRun()
//            throws Exception {
//        new Thread(server::run).start();
//        SECONDS.sleep(1);
//
//        assertTrue(isServerRun());
//    }
//
//    @Test
//    public void serverShouldBeStoppedBecauseOfInterrupting() {
//        final Thread serverThread = new Thread(server::run);
//        serverThread.start();
//        serverThread.interrupt();
//        assertFalse(isServerRun());
//    }
//
//    @Test
//    public void serverShouldBeStopped()
//            throws Exception {
//        final Thread runningServerThread = new Thread(server::run);
//        runningServerThread.start();
//        SECONDS.sleep(1);
//
//        server.stop();
//        runningServerThread.join();
//
//        assertFalse(isServerRun());
//    }
//
//    private static ProtocolServerConfig createServerConfiguration() {
//        return new ProtocolServerConfig(
//                GIVEN_HOST,
//                GIVEN_PORT,
//                GIVEN_THREAD_COUNT_PROCESSING_CONNECTION,
//                GIVEN_THREAD_COUNT_PROCESSING_DATA,
//                GIVEN_CONNECTION_LIFE_TIMEOUT_SECONDS
//        ) {
//        };
//    }
//
//    private static ProtocolServerConfig findConfiguration(final ProtocolServer<?, ?> server) {
//        return findProperty(server, FIELD_NAME_CONFIGURATION, ProtocolServerConfig.class);
//    }
//
//    private static ServerRunningContext findRunningContext(final ProtocolServer<?, ?> server) {
//        return findProperty(server, FIELD_NAME_RUNNING_CONTEXT, ServerRunningContext.class);
//    }
//
//    private static int findExecutorCount(final EventLoopGroup eventLoopGroup) {
//        return ((NioEventLoopGroup) eventLoopGroup).executorCount();
//    }
//
//    private static boolean isServerRun() {
//        try (final Socket ignored = new Socket(GIVEN_HOST, GIVEN_PORT)) {
//            return true;
//        } catch (final IOException exception) {
//            return false;
//        }
//    }
//
//    private static final class TestProtocolServer extends ProtocolServer<PackageDecoder<?, ?, ?>, PackageEncoder<?>> {
//        private final ProtocolDecoder<?, ?> protocolDecoder;
//        private final ProtocolEncoder protocolEncoder;
//        private final ProtocolHandler protocolHandler;
//
//        public TestProtocolServer(final ProtocolServerConfig configuration,
//                                  final ContextAttributeManager contextAttributeManager,
//                                  final ConnectionManager connectionManager,
//                                  final List<PackageDecoder<?, ?, ?>> packageDecoders,
//                                  final List<PackageEncoder<?>> packageEncoders,
//                                  final List<PackageHandler<?>> packageHandlers,
//                                  final ProtocolDecoder<?, ?> protocolDecoder,
//                                  final ProtocolEncoder protocolEncoder,
//                                  final ProtocolHandler protocolHandler) {
//            super(
//                    configuration,
//                    contextAttributeManager,
//                    connectionManager,
//                    packageDecoders,
//                    packageEncoders,
//                    packageHandlers
//            );
//            this.protocolDecoder = protocolDecoder;
//            this.protocolEncoder = protocolEncoder;
//            this.protocolHandler = protocolHandler;
//        }
//
//        @Override
//        protected ProtocolDecoder<?, ?> createProtocolDecoder(final ServerRunningContext context) {
//            return protocolDecoder;
//        }
//
//        @Override
//        protected ProtocolEncoder createProtocolEncoder(final ServerRunningContext context) {
//            return protocolEncoder;
//        }
//
//        @Override
//        protected ProtocolHandler createProtocolHandler(final ServerRunningContext context) {
//            return protocolHandler;
//        }
//    }
//}
