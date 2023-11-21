package by.bsu.wialontransport.protocol.core.service.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.LoginResponseProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;

@Service
public final class TrackerUnprotectedLoginService extends TrackerLoginService<LoginPackage, LoginResponseProvider> {

    public TrackerUnprotectedLoginService(final ContextAttributeManager contextAttributeManager,
                                          final TrackerService trackerService,
                                          final ConnectionManager connectionManager,
                                          final DataService dataService) {
        super(contextAttributeManager, trackerService, connectionManager, dataService);
    }

    @Override
    protected Optional<Package> checkPackageAndTakeResponseIfLoginFailed(
            final Tracker tracker,
            final LoginPackage loginPackage,
            final LoginResponseProvider responseProvider
    ) {
        return empty();
    }
}
