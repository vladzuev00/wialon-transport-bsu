package by.bsu.wialontransport.protocol.newwing.server;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer.ServerRunningContext;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.encoder.NewWingProtocolEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingPackageEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class NewWingProtocolServerTest extends AbstractContextTest {
    private static final String FIELD_NAME_PACKAGE_DECODERS = "packageDecoders";
    private static final String FIELD_NAME_PACKAGE_ENCODERS = "packageEncoders";
    private static final String FIELD_NAME_PACKAGE_HANDLERS = "packageHandlers";
    private static final String FIELD_NAME_CONTEXT_ATTRIBUTE_MANAGER = "contextAttributeManager";
    private static final String FIELD_NAME_CONNECTION_MANAGER = "connectionManager";

    @Autowired
    private NewWingProtocolServer server;

    @Test
    public void protocolDecoderShouldBeCreated() {
        final ServerRunningContext givenContext = mock(ServerRunningContext.class);
        final NewWingPackageDecoder<?, ?> firstGivenPackageDecoder = mock(NewWingPackageDecoder.class);
        final NewWingPackageDecoder<?, ?> secondGivenPackageDecoder = mock(NewWingPackageDecoder.class);
        final List<NewWingPackageDecoder<?, ?>> givenPackageDecoders = List.of(
                firstGivenPackageDecoder,
                secondGivenPackageDecoder
        );
        when(givenContext.getPackageDecoders()).thenReturn(givenPackageDecoders);

        @SuppressWarnings("unchecked") final NewWingProtocolDecoder actual = server.createProtocolDecoder(givenContext);
        final List<PackageDecoder<?, ?, ?>> actualPackageDecoders = findPackageDecoders(actual);
        assertSame(givenPackageDecoders, actualPackageDecoders);
    }

    @Test
    public void protocolEncoderShouldBeCreated() {
        final ServerRunningContext givenContext = mock(ServerRunningContext.class);
        final NewWingPackageEncoder<?> firstGivenPackageEncoder = mock(NewWingPackageEncoder.class);
        final NewWingPackageEncoder<?> secondGivenPackageEncoder = mock(NewWingPackageEncoder.class);
        final List<NewWingPackageEncoder<?>> givenPackageEncoders = List.of(
                firstGivenPackageEncoder,
                secondGivenPackageEncoder
        );
        when(givenContext.getPackageEncoders()).thenReturn(givenPackageEncoders);

        @SuppressWarnings("unchecked") final NewWingProtocolEncoder actual = server.createProtocolEncoder(givenContext);
        final List<PackageEncoder<?>> actualPackageEncoders = findPackageEncoders(actual);
        assertSame(givenPackageEncoders, actualPackageEncoders);
    }

    @Test
    public void protocolHandlerShouldBeCreated() {
        final ServerRunningContext givenContext = mock(ServerRunningContext.class);

        final PackageHandler<?> firstGivenPackageHandler = mock(PackageHandler.class);
        final PackageHandler<?> secondGivenPackageHandler = mock(PackageHandler.class);
        final List<PackageHandler<?>> givenPackageHandlers = List.of(
                firstGivenPackageHandler,
                secondGivenPackageHandler
        );
        when(givenContext.getPackageHandlers()).thenReturn(givenPackageHandlers);

        final ContextAttributeManager givenContextAttributeManager = mock(ContextAttributeManager.class);
        when(givenContext.getContextAttributeManager()).thenReturn(givenContextAttributeManager);

        final ConnectionManager givenConnectionManager = mock(ConnectionManager.class);
        when(givenContext.getConnectionManager()).thenReturn(givenConnectionManager);

        @SuppressWarnings("unchecked") final ProtocolHandler actual = server.createProtocolHandler(givenContext);

        final List<PackageHandler<?>> actualPackageHandlers = findPackageHandlers(actual);
        assertSame(givenPackageHandlers, actualPackageHandlers);

        final ContextAttributeManager actualContextAttributeManager = findContextAttributeManager(actual);
        assertSame(givenContextAttributeManager, actualContextAttributeManager);

        final ConnectionManager actualConnectionManager = findConnectionManager(actual);
        assertSame(givenConnectionManager, actualConnectionManager);
    }

    @SuppressWarnings("unchecked")
    private static List<PackageDecoder<?, ?, ?>> findPackageDecoders(final ProtocolDecoder<?, ?> protocolDecoder) {
        return findProperty(protocolDecoder, FIELD_NAME_PACKAGE_DECODERS, List.class);
    }

    @SuppressWarnings("unchecked")
    private static List<PackageEncoder<?>> findPackageEncoders(final ProtocolEncoder protocolEncoder) {
        return findProperty(protocolEncoder, FIELD_NAME_PACKAGE_ENCODERS, List.class);
    }

    @SuppressWarnings("unchecked")
    private static List<PackageHandler<?>> findPackageHandlers(final ProtocolHandler protocolHandler) {
        return findProperty(protocolHandler, FIELD_NAME_PACKAGE_HANDLERS, List.class);
    }

    private static ContextAttributeManager findContextAttributeManager(final ProtocolHandler protocolHandler) {
        return findProperty(protocolHandler, FIELD_NAME_CONTEXT_ATTRIBUTE_MANAGER, ContextAttributeManager.class);
    }

    private static ConnectionManager findConnectionManager(final ProtocolHandler protocolHandler) {
        return findProperty(protocolHandler, FIELD_NAME_CONNECTION_MANAGER, ConnectionManager.class);
    }
}
