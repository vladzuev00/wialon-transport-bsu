package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.config.property.protocolserver.WialonProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer;
import by.bsu.wialontransport.protocol.wialon.decoder.WialonProtocolDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.encoder.WialonProtocolEncoder;
import by.bsu.wialontransport.protocol.wialon.encoder.packages.WialonPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestLoginPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestPingPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.data.AbstractWialonRequestDataPackageHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

//TODO: test
@Component
public final class WialonProtocolServer extends ProtocolServer<WialonPackageDecoder<?>, WialonPackageEncoder<?>> {

    public WialonProtocolServer(final WialonProtocolServerConfiguration configuration,
                                final ContextAttributeManager contextAttributeManager,
                                final ConnectionManager connectionManager,
                                final List<WialonPackageDecoder<?>> packageDecoders,
                                final List<WialonPackageEncoder<?>> packageEncoders,
                                final WialonRequestLoginPackageHandler loginPackageHandler,
                                final WialonRequestPingPackageHandler pingPackageHandler,
                                final List<AbstractWialonRequestDataPackageHandler<?>> dataPackageHandlers) {
        super(
                configuration,
                contextAttributeManager,
                connectionManager,
                packageDecoders,
                packageEncoders,
                concat(loginPackageHandler, pingPackageHandler, dataPackageHandlers)
        );
    }

    @Override
    protected WialonProtocolDecoder createProtocolDecoder(final ServerRunningContext context) {
        return new WialonProtocolDecoder(context.getPackageDecoders());
    }

    @Override
    protected WialonProtocolEncoder createProtocolEncoder(final ServerRunningContext context) {
        return new WialonProtocolEncoder(context.getPackageEncoders());
    }

    @Override
    protected ProtocolHandler createProtocolHandler(final ServerRunningContext context) {
        return new ProtocolHandler(
                context.getPackageHandlers(),
                context.getContextAttributeManager(),
                context.getConnectionManager()
        );
    }

    private static List<PackageHandler<?>> concat(
            final WialonRequestLoginPackageHandler loginPackageHandler,
            final WialonRequestPingPackageHandler pingPackageHandler,
            final List<AbstractWialonRequestDataPackageHandler<?>> dataPackageHandlers
    ) {
        return Stream.concat(
                Stream.of(loginPackageHandler, pingPackageHandler),
                dataPackageHandlers.stream()
        ).toList();
    }
}
