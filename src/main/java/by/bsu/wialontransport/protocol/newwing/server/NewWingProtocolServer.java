package by.bsu.wialontransport.protocol.newwing.server;

import by.bsu.wialontransport.configuration.property.protocolserver.ProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.encoder.NewWingProtocolEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingPackageEncoder;

import java.util.List;

public final class NewWingProtocolServer extends ProtocolServer<NewWingPackageDecoder<?, ?>, NewWingPackageEncoder<?>> {

    public NewWingProtocolServer(final ProtocolServerConfiguration configuration,
                                 final ContextAttributeManager contextAttributeManager,
                                 final ConnectionManager connectionManager,
                                 final List<NewWingPackageDecoder<?, ?>> packageDecoders,
                                 final List<NewWingPackageEncoder<?>> newWingPackageEncoders,
                                 final List<? extends PackageHandler<?>> packageHandlers) {
        super(
                configuration,
                contextAttributeManager,
                connectionManager,
                packageDecoders,
                newWingPackageEncoders,
                packageHandlers
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
