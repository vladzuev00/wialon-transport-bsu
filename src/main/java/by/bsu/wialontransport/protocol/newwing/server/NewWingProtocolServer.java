package by.bsu.wialontransport.protocol.newwing.server;

import by.bsu.wialontransport.configuration.property.ProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.server.ProtocolServer;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.encoder.NewWingProtocolEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingPackageEncoder;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingDataPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingEventCountPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingLoginPackageHandler;

import java.util.List;

//public final class NewWingProtocolServer extends ProtocolServer {
//    private final List<NewWingPackageDecoder<?, ?>> packageDecoders;
//    private final List<NewWingPackageEncoder<?>> packageEncoders;
//    private final List<PackageHandler<?>> packageHandlers;
//
//    public NewWingProtocolServer(final ContextAttributeManager contextAttributeManager,
//                                 final ProtocolServerConfiguration configuration,
//                                 final List<NewWingPackageDecoder<?, ?>> packageDecoders,
//                                 final List<NewWingPackageEncoder<?>> packageEncoders,
//                                 final NewWingLoginPackageHandler loginPackageHandler,
//                                 final NewWingEventCountPackageHandler eventCountPackageHandler,
//                                 final NewWingDataPackageHandler dataPackageHandler) {
//        super(contextAttributeManager, configuration);
//        this.packageDecoders = packageDecoders;
//        this.packageEncoders = packageEncoders;
//        packageHandlers = List.of(loginPackageHandler, eventCountPackageHandler, dataPackageHandler);
//    }
//
//    @Override
//    protected NewWingProtocolDecoder createDecoder() {
//        return new NewWingProtocolDecoder(packageDecoders);
//    }
//
//    @Override
//    protected NewWingProtocolEncoder createEncoder() {
//        return new NewWingProtocolEncoder(packageEncoders);
//    }
//
//    @Override
//    protected ProtocolHandler createHandler(final ContextAttributeManager contextAttributeManager,
//                                            final ConnectionManager connectionManager) {
//        return new ProtocolHandler(packageHandlers, contextAttributeManager, );
//    }
//}
