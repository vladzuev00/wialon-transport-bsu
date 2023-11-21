package by.bsu.wialontransport.protocol.core.service.login;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.handler.packages.login.responseprovider.LoginResponseProvider;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class TrackerLoginService<PACKAGE extends LoginPackage, RESPONSE_PROVIDER extends LoginResponseProvider> {
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ConnectionManager connectionManager;
    private final DataService dataService;

    public final void login(final PACKAGE loginPackage,
                            final RESPONSE_PROVIDER responseProvider,
                            final ChannelHandlerContext context) {
        final String trackerImei = loginPackage.getImei();
        this.contextAttributeManager.putTrackerImei(context, trackerImei);
        final Optional<Tracker> optionalTracker = this.trackerService.findByImei(trackerImei);
        optionalTracker.ifPresentOrElse(
                tracker -> this.loginExistingTracker(tracker, loginPackage, responseProvider, context),
                () -> this.sendNoSuchImeiResponse(responseProvider, context)
        );
    }

    protected abstract Optional<Package> checkPackageAndTakeResponseIfLoginFailed(
            final Tracker tracker,
            final PACKAGE loginPackage,
            final RESPONSE_PROVIDER responseProvider
    );

    private void loginExistingTracker(final Tracker tracker,
                                      final PACKAGE loginPackage,
                                      final RESPONSE_PROVIDER responseProvider,
                                      final ChannelHandlerContext context) {
        final Optional<Package> optionalFailedResponse = this.checkPackageAndTakeResponseIfLoginFailed(
                tracker,
                loginPackage,
                responseProvider
        );
        optionalFailedResponse.ifPresentOrElse(
                context::writeAndFlush,
                () -> this.handleSuccessLogin(tracker, responseProvider, context)
        );
    }

    private void sendNoSuchImeiResponse(final RESPONSE_PROVIDER responseProvider, final ChannelHandlerContext context) {
        sendResponse(responseProvider::getNoSuchImeiResponse, context);
    }

    private void sendSuccessResponse(final RESPONSE_PROVIDER responseProvider, final ChannelHandlerContext context) {
        sendResponse(responseProvider::getSuccessResponse, context);
    }

    private static void sendResponse(final Supplier<Package> responseSupplier, final ChannelHandlerContext context) {
        final Package response = responseSupplier.get();
        context.writeAndFlush(response);
    }

    private void handleSuccessLogin(final Tracker tracker,
                                    final RESPONSE_PROVIDER responseProvider,
                                    final ChannelHandlerContext context) {
        this.contextAttributeManager.putTracker(context, tracker);
        this.connectionManager.add(context);
        this.putLastDataInContextIfExist(tracker, context);
        this.sendSuccessResponse(responseProvider, context);
    }

    private void putLastDataInContextIfExist(final Tracker tracker, final ChannelHandlerContext context) {
        final Optional<Data> optionalLastData = this.dataService.findTrackerLastData(tracker);
        optionalLastData.ifPresent(data -> this.contextAttributeManager.putLastData(context, data));
    }
}
