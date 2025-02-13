package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class LoginPackageHandler<REQUEST extends LoginPackage> extends PackageHandler<REQUEST> {
    private final TrackerImeiFactory imeiFactory;
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ChannelHandlerContextManager contextManager;
    private final LocationService locationService;

    public LoginPackageHandler(final Class<REQUEST> requestType,
                               final TrackerImeiFactory imeiFactory,
                               final ContextAttributeManager contextAttributeManager,
                               final TrackerService trackerService,
                               final ChannelHandlerContextManager contextManager,
                               final LocationService locationService) {
        super(requestType);
        this.imeiFactory = imeiFactory;
        this.contextAttributeManager = contextAttributeManager;
        this.trackerService = trackerService;
        this.contextManager = contextManager;
        this.locationService = locationService;
    }

    @Override
    protected final Object handleInternal(final REQUEST request, final ChannelHandlerContext context) {
        final String imei = imeiFactory.create(request);
        contextAttributeManager.putTrackerImei(context, imei);
        return trackerService.findByImei(imei)
                .map(tracker -> login(tracker, request, context))
                .orElseGet(this::createNoSuchImeiResponse);
    }

    protected abstract Object createNoSuchImeiResponse();

    protected abstract Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request);

    protected abstract Object createSuccessResponse();

    private Object login(final Tracker tracker, final REQUEST request, final ChannelHandlerContext context) {
        return loginCreatingFailedResponse(tracker, request).orElseGet(() -> handleSuccess(tracker, context));
    }

    private Object handleSuccess(final Tracker tracker, final ChannelHandlerContext context) {
        contextAttributeManager.putTracker(context, tracker);
        locationService.findLastFetchingParameters(tracker)
                .ifPresent(location -> contextAttributeManager.putLastLocation(context, location));
        contextManager.add(context);
        return createSuccessResponse();
    }
}
