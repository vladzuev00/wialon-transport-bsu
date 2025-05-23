package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.PackageHandler;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class LoginPackageHandler<REQUEST extends LoginPackage> extends PackageHandler<REQUEST> {
    private final TrackerImeiFactory imeiFactory;
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ContextManager contextManager;
    private final LocationService locationService;

    public LoginPackageHandler(final Class<REQUEST> requestType,
                               final TrackerImeiFactory imeiFactory,
                               final ContextAttributeManager contextAttributeManager,
                               final TrackerService trackerService,
                               final ContextManager contextManager,
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
        contextAttributeManager.putImei(context, imei);
        return trackerService.findByImei(imei)
                .map(tracker -> login(tracker, request, context))
                .orElseGet(this::createNoSuchImeiResponse);
    }

    protected abstract Object createNoSuchImeiResponse();

    protected abstract Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request);

    protected abstract void onSuccess();

    protected abstract Object createSuccessResponse();

    private Object login(final Tracker tracker, final REQUEST request, final ChannelHandlerContext context) {
        return loginCreatingFailedResponse(tracker, request).orElseGet(() -> handleSuccess(tracker, context));
    }

    private Object handleSuccess(final Tracker tracker, final ChannelHandlerContext context) {
        putTracker(context, tracker);
        putLastLocation(context, tracker);
        contextManager.add(context);
        onSuccess();
        return createSuccessResponse();
    }

    private void putTracker(final ChannelHandlerContext context, final Tracker tracker) {
        contextAttributeManager.putTracker(context, tracker);
    }

    private void putLastLocation(final ChannelHandlerContext context, final Tracker tracker) {
        locationService.findLastFetchingParameters(tracker)
                .ifPresent(location -> contextAttributeManager.putLastLocation(context, location));
    }
}
