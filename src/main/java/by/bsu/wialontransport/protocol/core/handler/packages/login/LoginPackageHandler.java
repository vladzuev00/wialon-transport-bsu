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
    protected final Package handleInternal(final PACKAGE request, final ChannelHandlerContext context) {
        final String imei = request.getImei();
        contextAttributeManager.putTrackerImei(context, imei);
        return trackerService.findByImei(imei)
                .map(tracker -> login(tracker, request, context))
                .orElseGet(this::createNoSuchImeiResponse);
    }

    protected abstract Optional<Package> loginCreatingResponseIfFailed(final Tracker tracker, final PACKAGE request);

    protected abstract Package createNoSuchImeiResponse();

    protected abstract Package createSuccessResponse();

    private Package login(final Tracker tracker, final PACKAGE request, final ChannelHandlerContext context) {
        return loginCreatingResponseIfFailed(tracker, request).orElseGet(() -> handleSuccessLogin(tracker, context));
    }

    private Package handleSuccessLogin(final Tracker tracker, final ChannelHandlerContext context) {
        contextAttributeManager.putTracker(context, tracker);
        //TODO: do after putLastDataInContext(tracker, context);
        connectionManager.add(context);
        putLastDataInContext(tracker, context);
        return createSuccessResponse();
    }

    private void putLastDataInContext(final Tracker tracker, final ChannelHandlerContext context) {
        dataService.findTrackerLastDataFetchingParameters(tracker)
                .ifPresent(data -> contextAttributeManager.putLastData(context, data));
    }
}
