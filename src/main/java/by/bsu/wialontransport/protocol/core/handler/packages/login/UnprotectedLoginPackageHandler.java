package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;

import java.util.Optional;

import static java.util.Optional.empty;

//TODO: do the same as ProtectedLoginPackageHandler
public abstract class UnprotectedLoginPackageHandler extends LoginPackageHandler<LoginPackage> {

    public UnprotectedLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
                                          final TrackerService trackerService,
                                          final ConnectionManager connectionManager,
                                          final DataService dataService) {
        super(LoginPackage.class, contextAttributeManager, trackerService, connectionManager, dataService);
    }

    @Override
    protected final Optional<Package> loginCreatingResponseIfFailed(final Tracker tracker, final LoginPackage request) {
        return empty();
    }
}
