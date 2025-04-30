package by.vladzuev.locationreceiver.protocol.wialon.handler;

import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.ProtectedLoginPackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonRequestLoginPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage;
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
        return new WialonResponseLoginPackage(CONNECTION_FAILURE);
    }

    @Override
    protected WialonResponseLoginPackage createSuccessResponse() {
        return new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
    }

    @Override
    protected WialonResponseLoginPackage createWrongPasswordResponse() {
        return new WialonResponseLoginPackage(PASSWORD_ERROR);
    }
}
