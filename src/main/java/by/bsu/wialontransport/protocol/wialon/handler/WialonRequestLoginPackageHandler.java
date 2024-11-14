package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.login.ProtectedLoginPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.Status.*;

//TODO: test
@Component
public final class WialonRequestLoginPackageHandler extends ProtectedLoginPackageHandler<WialonRequestLoginPackage> {

    public WialonRequestLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                            final ContextAttributeManager contextAttributeManager,
                                            final TrackerService trackerService,
                                            final ChannelHandlerContextManager contextManager,
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
