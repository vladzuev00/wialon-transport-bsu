package by.bsu.wialontransport.protocol.core.service.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.ProtectedLoginPackage;
import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.ProtectedLoginResponseProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;

@Service
public final class TrackerProtectedLoginService
        extends TrackerLoginService<ProtectedLoginPackage, ProtectedLoginResponseProvider> {
    private final BCryptPasswordEncoder passwordEncoder;

    public TrackerProtectedLoginService(final ContextAttributeManager contextAttributeManager,
                                        final TrackerService trackerService,
                                        final ConnectionManager connectionManager,
                                        final DataService dataService,
                                        final BCryptPasswordEncoder passwordEncoder) {
        super(contextAttributeManager, trackerService, connectionManager, dataService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Optional<Package> checkPackageAndTakeResponseIfLoginFailed(
            final Tracker tracker,
            final ProtectedLoginPackage loginPackage,
            final ProtectedLoginResponseProvider responseProvider
    ) {
        final Package wrongPasswordResponse = responseProvider.getWrongPasswordResponse();
        return this.isPasswordCorrect(tracker, loginPackage) ? empty() : Optional.of(wrongPasswordResponse);
    }

    private boolean isPasswordCorrect(final Tracker tracker, final ProtectedLoginPackage loginPackage) {
        final String packagePassword = loginPackage.getPassword();
        final String trackerEncodedPassword = tracker.getPassword();
        return this.passwordEncoder.matches(packagePassword, trackerEncodedPassword);
    }
}
