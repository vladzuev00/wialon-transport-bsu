package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class LoginPackageHandler<REQUEST extends LoginPackage> extends PackageHandler<REQUEST> {
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ChannelHandlerContextManager contextManager;
    private final LocationService locationService;

    public LoginPackageHandler(final Class<REQUEST> requestType,
                               final ContextAttributeManager contextAttributeManager,
                               final TrackerService trackerService,
                               final ChannelHandlerContextManager contextManager,
                               final LocationService locationService) {
        super(requestType);
        this.contextAttributeManager = contextAttributeManager;
        this.trackerService = trackerService;
        this.contextManager = contextManager;
        this.locationService = locationService;
    }

    @Override
    protected final Object handleInternal(final REQUEST request, final ChannelHandlerContext context) {
        memorizeImei(context, request);
        return loginByImei(context, request);
    }

    protected abstract Object createNoSuchImeiResponse();

    protected abstract Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request);

    protected abstract Object createSuccessResponse();

    private void memorizeImei(final ChannelHandlerContext context, final REQUEST request) {
        contextAttributeManager.putTrackerImei(context, request.getImei());
    }

    private void memorizeTracker(final ChannelHandlerContext context, final Tracker tracker) {
        contextAttributeManager.putTracker(context, tracker);
    }

    private void memorizeLastLocation(final ChannelHandlerContext context, final Tracker tracker) {
        locationService.findLastLocationFetchingParameters(tracker)
                .ifPresent(location -> contextAttributeManager.putLastLocation(context, location));
    }

    private Object loginByImei(final ChannelHandlerContext context, final REQUEST request) {
        return trackerService.findByImei(request.getImei())
                .map(tracker -> login(context, tracker, request))
                .orElseGet(this::createNoSuchImeiResponse);
    }

    private Object login(final ChannelHandlerContext context, final Tracker tracker, final REQUEST request) {
        return loginCreatingFailedResponse(tracker, request).orElseGet(() -> handleSuccessLogin(context, tracker));
    }

    private Object handleSuccessLogin(final ChannelHandlerContext context, final Tracker tracker) {
        memorizeTracker(context, tracker);
        memorizeLastLocation(context, tracker);
        contextManager.add(context);
        return createSuccessResponse();
    }
}
