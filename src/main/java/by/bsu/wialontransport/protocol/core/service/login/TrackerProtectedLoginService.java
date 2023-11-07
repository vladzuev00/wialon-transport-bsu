package by.bsu.wialontransport.protocol.core.service.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.ProtectedLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static java.util.Optional.empty;

public abstract class TrackerProtectedLoginService<PACKAGE extends ProtectedLoginPackage>
        extends TrackerLoginService<PACKAGE> {
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
    protected final Optional<Package> checkPackageAndCreateResponseIfLoginFailed(final Tracker tracker,
                                                                                 final PACKAGE loginPackage) {
        return this.isPasswordCorrect(tracker, loginPackage)
                ? empty()
                : Optional.of(this.createWrongPasswordResponse());
    }

    protected abstract Package createWrongPasswordResponse();

    private boolean isPasswordCorrect(final Tracker tracker, final PACKAGE loginPackage) {
        final String packagePassword = loginPackage.getPassword();
        final String trackerEncodedPassword = tracker.getPassword();
        return this.passwordEncoder.matches(packagePassword, trackerEncodedPassword);
    }
}
