package by.bsu.wialontransport.protocol.core.service.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.LoginPackage;
import by.bsu.wialontransport.protocol.core.model.packages.Package;

import java.util.Optional;

import static java.util.Optional.empty;

public abstract class TrackerUnprotectedLoginService<PACKAGE extends LoginPackage>
        extends TrackerLoginService<PACKAGE> {

    public TrackerUnprotectedLoginService(final ContextAttributeManager contextAttributeManager,
                                          final TrackerService trackerService,
                                          final ConnectionManager connectionManager,
                                          final DataService dataService) {
        super(contextAttributeManager, trackerService, connectionManager, dataService);
    }

    @Override
    protected final Optional<Package> checkPackageAndCreateResponseIfLoginFailed(final Tracker tracker,
                                                                                 final PACKAGE loginPackage) {
        return empty();
    }
}
