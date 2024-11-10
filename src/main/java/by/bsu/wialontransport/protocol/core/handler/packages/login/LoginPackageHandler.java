package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class LoginPackageHandler<REQUEST extends LoginPackage> extends PackageHandler<REQUEST> {
    private static final String IMEI_TEMPLATE = "%20s";
    private static final char SPACE = ' ';
    private static final char

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
        memorizeImei(request, context);
        return loginByImei(request, context);
    }

    protected abstract Object createNoSuchImeiResponse();

    protected abstract Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final REQUEST request);

    protected abstract Object createSuccessResponse();

    private String getImei(final REQUEST request) {
        return IMEI_TEMPLATE.formatted(request.getImei()).replace(' ', '0');
    }

    private void memorizeImei(final REQUEST request, final ChannelHandlerContext context) {
        contextAttributeManager.putTrackerImei(context, request.getImei());
    }

    private void memorizeTracker(final Tracker tracker, final ChannelHandlerContext context) {
        contextAttributeManager.putTracker(context, tracker);
    }

    private void memorizeLastLocation(final Tracker tracker, final ChannelHandlerContext context) {
        locationService.findLastLocationFetchingParameters(tracker)
                .ifPresent(location -> contextAttributeManager.putLastLocation(context, location));
    }

    private void memorizeContext(final ChannelHandlerContext context) {
        contextManager.add(context);
    }

    private Object loginByImei(final REQUEST request, final ChannelHandlerContext context) {
        return trackerService.findByImei(request.getImei())
                .map(tracker -> login(tracker, request, context))
                .orElseGet(this::createNoSuchImeiResponse);
    }

    private Object login(final Tracker tracker, final REQUEST request, final ChannelHandlerContext context) {
        return loginCreatingFailedResponse(tracker, request).orElseGet(() -> handleSuccessLogin(tracker, context));
    }

    private Object handleSuccessLogin(final Tracker tracker, final ChannelHandlerContext context) {
        memorizeTracker(tracker, context);
        memorizeLastLocation(tracker, context);
        memorizeContext(context);
        return createSuccessResponse();
    }
}
