package by.vladzuev.locationreceiver.protocol.jt808.handler;

import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808RegistrationPackage;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808ResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class JT808RegistrationPackageHandler extends UnprotectedLoginPackageHandler<JT808RegistrationPackage> {

    public JT808RegistrationPackageHandler(final TrackerImeiFactory imeiFactory,
                                           final ContextAttributeManager contextAttributeManager,
                                           final TrackerService trackerService,
                                           final ContextManager contextManager,
                                           final LocationService locationService) {
        super(
                JT808RegistrationPackage.class,
                imeiFactory,
                contextAttributeManager,
                trackerService,
                contextManager,
                locationService
        );
    }

    @Override
    protected JT808ResponsePackage createNoSuchImeiResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onSuccess() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected JT808ResponsePackage createSuccessResponse() {
        throw new UnsupportedOperationException();
    }
}
