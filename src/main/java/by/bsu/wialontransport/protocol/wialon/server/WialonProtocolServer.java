package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.config.property.protocolserver.WialonProtocolServerConfig;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer;
import by.bsu.wialontransport.protocol.wialon.decoder.WialonProtocolDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.encoder.WialonProtocolEncoder;
import by.bsu.wialontransport.protocol.wialon.encoder.packages.WialonPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestLoginPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestPingPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.data.WialonRequestBlackBoxPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.data.WialonRequestDataPackageHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class WialonProtocolServer extends ProtocolServer<WialonPackageDecoder, WialonPackageEncoder<?>> {

    public WialonProtocolServer(final WialonProtocolServerConfig configuration,
                                final ContextAttributeManager contextAttributeManager,
                                final ConnectionManager connectionManager,
                                final List<WialonPackageDecoder> packageDecoders,
                                final List<WialonPackageEncoder<?>> packageEncoders,
                                final WialonRequestLoginPackageHandler loginPackageHandler,
                                final WialonRequestPingPackageHandler pingPackageHandler,
                                final WialonRequestDataPackageHandler dataPackageHandler,
                                final WialonRequestBlackBoxPackageHandler blackBoxPackageHandler) {
        super(
                configuration,
                contextAttributeManager,
                connectionManager,
                packageDecoders,
                packageEncoders,
                List.of(loginPackageHandler, pingPackageHandler, dataPackageHandler, blackBoxPackageHandler)
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
}
