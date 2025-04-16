package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;

import java.util.Optional;

import static java.util.Optional.empty;

public abstract class UnprotectedLoginPackageHandler<REQUEST extends LoginPackage> extends LoginPackageHandler<REQUEST> {

    public UnprotectedLoginPackageHandler(final Class<REQUEST> requestType,
                                          final TrackerImeiFactory imeiFactory,
                                          final ContextAttributeManager contextAttributeManager,
                                          final TrackerService trackerService,
                                          final ContextManager contextManager,
                                          final LocationService locationService) {
        super(requestType, imeiFactory, contextAttributeManager, trackerService, contextManager, locationService);
    }

    @Override
    protected final Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request) {
        return empty();
    }
}
