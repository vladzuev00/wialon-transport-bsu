package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;

import java.util.Optional;

import static java.util.Optional.empty;

//TODO: test
public abstract class UnprotectedLoginPackageHandler<REQUEST extends LoginPackage> extends LoginPackageHandler<REQUEST> {

    public UnprotectedLoginPackageHandler(final Class<REQUEST> requestType,
                                          final TrackerImeiFactory imeiFactory,
                                          final ContextAttributeManager contextAttributeManager,
                                          final TrackerService trackerService,
                                          final ChannelHandlerContextManager contextManager,
                                          final LocationService locationService) {
        super(requestType, imeiFactory, contextAttributeManager, trackerService, contextManager, locationService);
    }

    @Override
    protected final Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request) {
        return empty();
    }
}
