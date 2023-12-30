package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class LoginPackageHandler<PACKAGE extends LoginPackage> extends PackageHandler<PACKAGE> {
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ConnectionManager connectionManager;
    private final DataService dataService;

    public LoginPackageHandler(final Class<PACKAGE> handledPackageType,
                               final ContextAttributeManager contextAttributeManager,
                               final TrackerService trackerService,
                               final ConnectionManager connectionManager,
                               final DataService dataService) {
        super(handledPackageType);
        this.contextAttributeManager = contextAttributeManager;
        this.trackerService = trackerService;
        this.connectionManager = connectionManager;
        this.dataService = dataService;
    }

    @Override
    protected final void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        final String trackerImei = requestPackage.getImei();
        this.contextAttributeManager.putTrackerImei(context, trackerImei);
        final Optional<Tracker> optionalTracker = this.trackerService.findByImei(trackerImei);
        optionalTracker.ifPresentOrElse(
                tracker -> this.loginExistingTracker(tracker, requestPackage, context),
                () -> this.sendNoSuchImeiResponse(context)
        );
    }

    protected abstract Optional<Package> checkLoginCreatingResponseIfFailed(final Tracker tracker,
                                                                            final PACKAGE loginPackage);

    protected abstract Package createNoSuchImeiResponse();

    protected abstract Package createSuccessResponse();

    private void sendNoSuchImeiResponse(final ChannelHandlerContext context) {
        sendResponse(this::createNoSuchImeiResponse, context);
    }

    private void sendSuccessResponse(final ChannelHandlerContext context) {
        sendResponse(this::createSuccessResponse, context);
    }

    private static void sendResponse(final Supplier<Package> responseSupplier, final ChannelHandlerContext context) {
        final Package response = responseSupplier.get();
        context.writeAndFlush(response);
    }

    private void loginExistingTracker(final Tracker tracker,
                                      final PACKAGE loginPackage,
                                      final ChannelHandlerContext context) {
        final Optional<Package> optionalFailedResponse = this.checkLoginCreatingResponseIfFailed(tracker, loginPackage);
        optionalFailedResponse.ifPresentOrElse(
                context::writeAndFlush,
                () -> this.handleSuccessLogin(tracker, context)
        );
    }

    private void handleSuccessLogin(final Tracker tracker, final ChannelHandlerContext context) {
        this.contextAttributeManager.putTracker(context, tracker);
        this.connectionManager.add(context);
        this.putLastDataInContextIfExist(tracker, context);
        this.sendSuccessResponse(context);
    }

    private void putLastDataInContextIfExist(final Tracker tracker, final ChannelHandlerContext context) {
//        final Optional<Data> optionalLastData = this.dataService.findTrackerLastData(tracker);
//        optionalLastData.ifPresent(data -> this.contextAttributeManager.putLastData(context, data));
    }
}
