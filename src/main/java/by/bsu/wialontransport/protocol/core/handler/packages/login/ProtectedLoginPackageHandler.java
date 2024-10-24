package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.ProtectedLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static java.util.Optional.empty;

//TODO: refactor tests
public abstract class ProtectedLoginPackageHandler<PACKAGE extends ProtectedLoginPackage>
        extends LoginPackageHandler<PACKAGE> {
    private final BCryptPasswordEncoder passwordEncoder;

    public ProtectedLoginPackageHandler(final Class<PACKAGE> handledPackageType,
                                        final ContextAttributeManager contextAttributeManager,
                                        final TrackerService trackerService,
                                        final ConnectionManager connectionManager,
                                        final DataService dataService,
                                        final BCryptPasswordEncoder passwordEncoder) {
        super(handledPackageType, contextAttributeManager, trackerService, connectionManager, dataService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected final Optional<Package> loginCreatingResponseIfFailed(final Tracker tracker,
                                                                    final ProtectedLoginPackage request) {
        return isPasswordCorrect(tracker, request) ? empty() : Optional.of(createWrongPasswordResponse());
    }

    protected abstract Package createWrongPasswordResponse();

    private boolean isPasswordCorrect(final Tracker tracker, final ProtectedLoginPackage request) {
        return passwordEncoder.matches(request.getPassword(), tracker.getPassword());
    }
}
