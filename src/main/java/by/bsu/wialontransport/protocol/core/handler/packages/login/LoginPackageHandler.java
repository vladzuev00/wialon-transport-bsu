package by.bsu.wialontransport.protocol.core.handler.packages.login;

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
        contextAttributeManager.putTrackerImei(context, trackerImei);
        trackerService.findByImei(trackerImei)
                .ifPresentOrElse(
                        tracker -> loginExistingTracker(tracker, requestPackage, context),
                        () -> sendNoSuchImeiResponse(context)
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
        context.writeAndFlush(responseSupplier.get());
    }

    private void loginExistingTracker(final Tracker tracker,
                                      final PACKAGE loginPackage,
                                      final ChannelHandlerContext context) {
        checkLoginCreatingResponseIfFailed(tracker, loginPackage)
                .ifPresentOrElse(
                        context::writeAndFlush,
                        () -> handleSuccessLogin(tracker, context)
                );
    }

    private void handleSuccessLogin(final Tracker tracker, final ChannelHandlerContext context) {
        contextAttributeManager.putTracker(context, tracker);
        connectionManager.add(context);
        putLastDataInContext(tracker, context);
        sendSuccessResponse(context);
    }

    private void putLastDataInContext(final Tracker tracker, final ChannelHandlerContext context) {
        dataService.findTrackerLastDataFetchingParameters(tracker)
                .ifPresent(data -> contextAttributeManager.putLastData(context, data));
    }
}
