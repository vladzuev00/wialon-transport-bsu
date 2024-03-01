package by.bsu.wialontransport.protocol.newwing.server;

import by.bsu.wialontransport.config.property.protocolserver.NewWingProtocolServerConfig;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.encoder.NewWingProtocolEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingPackageEncoder;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingDataPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingEventCountPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingLoginPackageHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class NewWingProtocolServer extends ProtocolServer<NewWingPackageDecoder<?, ?>, NewWingPackageEncoder<?>> {

    public NewWingProtocolServer(final NewWingProtocolServerConfig configuration,
                                 final ContextAttributeManager contextAttributeManager,
                                 final ConnectionManager connectionManager,
                                 final List<NewWingPackageDecoder<?, ?>> packageDecoders,
                                 final List<NewWingPackageEncoder<?>> packageEncoders,
                                 final NewWingLoginPackageHandler loginPackageHandler,
                                 final NewWingEventCountPackageHandler eventCountPackageHandler,
                                 final NewWingDataPackageHandler dataPackageHandler) {
        super(
                configuration,
                contextAttributeManager,
                connectionManager,
                packageDecoders,
                packageEncoders,
                List.of(loginPackageHandler, eventCountPackageHandler, dataPackageHandler)
        );
    }

    @Override
    protected NewWingProtocolDecoder createProtocolDecoder(final ServerRunningContext context) {
        return new NewWingProtocolDecoder(context.getPackageDecoders());
    }

    @Override
    protected NewWingProtocolEncoder createProtocolEncoder(final ServerRunningContext context) {
        return new NewWingProtocolEncoder(context.getPackageEncoders());
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
