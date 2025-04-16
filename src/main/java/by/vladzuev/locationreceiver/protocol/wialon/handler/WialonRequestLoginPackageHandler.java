package by.vladzuev.locationreceiver.protocol.wialon.handler;

import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.contextattributemanager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.contextmanager.ContextManager;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.ProtectedLoginPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestLoginPackageHandler extends ProtectedLoginPackageHandler<WialonRequestLoginPackage> {

    public WialonRequestLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                            final ContextAttributeManager contextAttributeManager,
                                            final TrackerService trackerService,
                                            final ContextManager contextManager,
                                            final LocationService locationService,
                                            final BCryptPasswordEncoder passwordEncoder) {
        super(
                WialonRequestLoginPackage.class,
                imeiFactory,
                contextAttributeManager,
                trackerService,
                contextManager,
                locationService,
                passwordEncoder
        );
    }

    @Override
    protected WialonResponseLoginPackage createNoSuchImeiResponse() {
        return new WialonResponseLoginPackage(WialonResponseLoginPackage.Status.CONNECTION_FAILURE);
    }

    @Override
    protected WialonResponseLoginPackage createSuccessResponse() {
        return new WialonResponseLoginPackage(WialonResponseLoginPackage.Status.SUCCESS_AUTHORIZATION);
    }

    @Override
    protected WialonResponseLoginPackage createWrongPasswordResponse() {
        return new WialonResponseLoginPackage(WialonResponseLoginPackage.Status.PASSWORD_ERROR);
    }
}
