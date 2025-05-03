package by.vladzuev.locationreceiver.protocol.teltonika.handler;

import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseFailedLoginPackage;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseSuccessLoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaLoginPackageHandler extends UnprotectedLoginPackageHandler<TeltonikaRequestLoginPackage> {

    public TeltonikaLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                        final ContextAttributeManager contextAttributeManager,
                                        final TrackerService trackerService,
                                        final ContextManager contextManager,
                                        final LocationService locationService) {
        super(
                TeltonikaRequestLoginPackage.class,
                imeiFactory,
                contextAttributeManager,
                trackerService,
                contextManager,
                locationService
        );
    }

    @Override
    protected TeltonikaResponseFailedLoginPackage createNoSuchImeiResponse() {
        return new TeltonikaResponseFailedLoginPackage();
    }

    @Override
    protected TeltonikaResponseSuccessLoginPackage createSuccessResponse() {
        return new TeltonikaResponseSuccessLoginPackage();
    }
}
