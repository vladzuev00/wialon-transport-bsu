package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public abstract class ProtectedLoginPackageHandler<REQUEST extends ProtectedLoginPackage> extends LoginPackageHandler<REQUEST> {
    private final BCryptPasswordEncoder passwordEncoder;

    public ProtectedLoginPackageHandler(final Class<REQUEST> requestType,
                                        final TrackerImeiFactory imeiFactory,
                                        final ContextAttributeManager contextAttributeManager,
                                        final TrackerService trackerService,
                                        final ContextManager contextManager,
                                        final LocationService locationService,
                                        final BCryptPasswordEncoder passwordEncoder) {
        super(requestType, imeiFactory, contextAttributeManager, trackerService, contextManager, locationService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected final Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request) {
        return Optional.of(request)
                .map(ProtectedLoginPackage::getPassword)
                .filter(password -> isWrongPassword(password, tracker))
                .map(password -> createWrongPasswordResponse());
    }

    protected abstract Object createWrongPasswordResponse();

    private boolean isWrongPassword(final String password, final Tracker tracker) {
        return !passwordEncoder.matches(password, tracker.getPassword());
    }
}
