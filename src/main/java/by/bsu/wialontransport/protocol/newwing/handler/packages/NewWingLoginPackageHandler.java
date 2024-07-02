//package by.bsu.wialontransport.protocol.newwing.handler.packages;
//
//import by.bsu.wialontransport.crud.service.DataService;
//import by.bsu.wialontransport.crud.service.TrackerService;
//import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingFailureResponsePackage;
//import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;
//import org.springframework.stereotype.Component;
//
//@Component
//public final class NewWingLoginPackageHandler extends UnprotectedLoginPackageHandler {
//
//    public NewWingLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
//                                      final TrackerService trackerService,
//                                      final ConnectionManager connectionManager,
//                                      final DataService dataService) {
//        super(contextAttributeManager, trackerService, connectionManager, dataService);
//    }
//
//    @Override
//    protected Package createNoSuchImeiResponse() {
//        return new NewWingFailureResponsePackage();
//    }
//
//    @Override
//    protected Package createSuccessResponse() {
//        return new NewWingSuccessResponsePackage();
//    }
//}
