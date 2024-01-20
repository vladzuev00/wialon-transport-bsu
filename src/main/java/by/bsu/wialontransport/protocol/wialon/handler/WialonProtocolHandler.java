package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestLoginPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.WialonRequestPingPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.data.WialonRequestBlackBoxPackageHandler;
import by.bsu.wialontransport.protocol.wialon.handler.packages.data.WialonRequestDataPackageHandler;

import java.util.List;

public final class WialonProtocolHandler extends ProtocolHandler {

    public WialonProtocolHandler(final WialonRequestLoginPackageHandler loginPackageHandler,
                                 final WialonRequestPingPackageHandler pingPackageHandler,
                                 final WialonRequestDataPackageHandler dataPackageHandler,
                                 final WialonRequestBlackBoxPackageHandler blackBoxPackageHandler,
                                 final ContextAttributeManager contextAttributeManager,
                                 final ConnectionManager connectionManager) {
        super(
                List.of(loginPackageHandler, pingPackageHandler, dataPackageHandler, blackBoxPackageHandler),
                contextAttributeManager,
                connectionManager
        );
    }

}
