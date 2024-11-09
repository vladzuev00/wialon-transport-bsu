package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.model.login.ProtectedLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static java.util.Optional.empty;

public abstract class ProtectedLoginPackageHandler<REQUEST extends ProtectedLoginPackage> extends LoginPackageHandler<REQUEST> {
    private final BCryptPasswordEncoder passwordEncoder;

    public ProtectedLoginPackageHandler(final Class<REQUEST> requestType,
                                        final ContextAttributeManager contextAttributeManager,
                                        final TrackerService trackerService,
                                        final ChannelHandlerContextManager contextManager,
                                        final LocationService locationService,
                                        final BCryptPasswordEncoder passwordEncoder) {
        super(requestType, contextAttributeManager, trackerService, contextManager, locationService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected final Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request) {
        return isPasswordCorrect(tracker, request) ? empty() : Optional.of(createWrongPasswordResponse());
    }

    protected abstract Object createWrongPasswordResponse();

    private boolean isPasswordCorrect(final Tracker tracker, final REQUEST request) {
        return passwordEncoder.matches(request.getPassword(), tracker.getPassword());
    }
}
