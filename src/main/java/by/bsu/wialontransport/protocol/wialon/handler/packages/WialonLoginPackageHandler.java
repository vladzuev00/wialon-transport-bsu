package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.login.ProtectedLoginPackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status.*;

public final class WialonLoginPackageHandler extends ProtectedLoginPackageHandler {

    public WialonLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
                                     final TrackerService trackerService,
                                     final ConnectionManager connectionManager,
                                     final DataService dataService,
                                     final BCryptPasswordEncoder passwordEncoder) {
        super(contextAttributeManager, trackerService, connectionManager, dataService, passwordEncoder);
    }

    @Override
    protected Package createNoSuchImeiResponse() {
        return new WialonResponseLoginPackage(CONNECTION_FAILURE);
    }

    @Override
    protected Package createSuccessResponse() {
        return new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
    }

    @Override
    protected Package createWrongPasswordResponse() {
        return new WialonResponseLoginPackage(ERROR_CHECK_PASSWORD);
    }
}