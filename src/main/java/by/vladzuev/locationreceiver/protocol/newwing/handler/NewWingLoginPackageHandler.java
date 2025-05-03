package by.vladzuev.locationreceiver.protocol.newwing.handler;

import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLoginPackage;
import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingFailureResponsePackage;
import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingLoginPackageHandler extends UnprotectedLoginPackageHandler<NewWingLoginPackage> {

    public NewWingLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                      final ContextAttributeManager contextAttributeManager,
                                      final TrackerService trackerService,
                                      final ContextManager contextManager,
                                      final LocationService locationService) {
        super(
                NewWingLoginPackage.class,
                imeiFactory,
                contextAttributeManager,
                trackerService,
                contextManager,
                locationService
        );
    }

    @Override
    protected NewWingFailureResponsePackage createNoSuchImeiResponse() {
        return new NewWingFailureResponsePackage();
    }

    @Override
    protected void onSuccess() {

    }

    @Override
    protected NewWingSuccessResponsePackage createSuccessResponse() {
        return new NewWingSuccessResponsePackage();
    }
}
