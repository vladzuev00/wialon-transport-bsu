//package by.bsu.wialontransport.protocol.wialon.handler.packages;
//
//import by.bsu.wialontransport.crud.service.DataService;
//import by.bsu.wialontransport.crud.service.TrackerService;
//import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.handler.packages.login.ProtectedLoginPackageHandler;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
//import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.Status.*;
//
//@Component
//public final class WialonRequestLoginPackageHandler extends ProtectedLoginPackageHandler<WialonRequestLoginPackage> {
//
//    public WialonRequestLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
//                                            final TrackerService trackerService,
//                                            final ConnectionManager connectionManager,
//                                            final DataService dataService,
//                                            final BCryptPasswordEncoder passwordEncoder) {
//        super(
//                WialonRequestLoginPackage.class,
//                contextAttributeManager,
//                trackerService,
//                connectionManager,
//                dataService,
//                passwordEncoder
//        );
//    }
//
//    @Override
//    protected Package createNoSuchImeiResponse() {
//        return new WialonResponseLoginPackage(CONNECTION_FAILURE);
//    }
//
//    @Override
//    protected Package createSuccessResponse() {
//        return new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
//    }
//
//    @Override
//    protected Package createWrongPasswordResponse() {
//        return new WialonResponseLoginPackage(ERROR_CHECK_PASSWORD);
//    }
//}
