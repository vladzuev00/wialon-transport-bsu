package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingDataPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingEventCountPackageHandler;
import by.bsu.wialontransport.protocol.newwing.handler.packages.NewWingLoginPackageHandler;

import java.util.List;

public final class NewWingProtocolHandler extends ProtocolHandler {

    public NewWingProtocolHandler(final NewWingLoginPackageHandler loginPackageHandler,
                                  final NewWingEventCountPackageHandler eventCountPackageHandler,
                                  final NewWingDataPackageHandler dataPackageHandler,
                                  final ContextAttributeManager contextAttributeManager,
                                  final ConnectionManager connectionManager) {
        super(
                List.of(loginPackageHandler, eventCountPackageHandler, dataPackageHandler),
                contextAttributeManager,
                connectionManager);
    }

}
