package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;

import java.util.List;

public final class NewWingProtocolHandler extends ProtocolHandler {

    public NewWingProtocolHandler(final List<PackageHandler<?>> packageHandlers,
                                  final ContextAttributeManager contextAttributeManager,
                                  final ConnectionManager connectionManager) {
        super(packageHandlers, contextAttributeManager, connectionManager);
    }

}
