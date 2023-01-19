package by.bsu.wialontransport.protocol.core.service;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.Status;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.Status.*;

@Service
@RequiredArgsConstructor
public final class AuthorizationTrackerService {
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ConnectionManager connectionManager;
    private final DataService dataService;

    public void authorize(final RequestLoginPackage requestPackage, final ChannelHandlerContext context) {
        this.contextAttributeManager.putTrackerImei(context, requestPackage.getImei());
        final Optional<Tracker> optionalTracker = this.trackerService.findByImei(requestPackage.getImei());
        final Status status = optionalTracker
                .map(tracker -> checkPassword(tracker, requestPackage.getPassword()))
                .orElse(CONNECTION_FAILURE);
        if (status == SUCCESS_AUTHORIZATION) {
            final Tracker tracker = optionalTracker.get();
            this.contextAttributeManager.putTracker(context, tracker);
            this.connectionManager.add(context);
            this.putLastDataIfExist(context, tracker);
        }
        sendResponse(context, status);
    }

    private static Status checkPassword(final Tracker tracker, final String packagePassword) {
        final String devicePassword = tracker.getPassword();
        return packagePassword.equals(devicePassword) ? SUCCESS_AUTHORIZATION : ERROR_CHECK_PASSWORD;
    }

    private void putLastDataIfExist(final ChannelHandlerContext context, final Tracker tracker) {
        final Optional<Data> optionalTrackerLastData = this.dataService.findTrackerLastData(tracker.getId());
        optionalTrackerLastData.ifPresent(data -> this.contextAttributeManager.putLastData(context, data));
    }

    private static void sendResponse(final ChannelHandlerContext context, final Status status) {
        final ResponseLoginPackage responseLoginPackage = new ResponseLoginPackage(status);
        context.writeAndFlush(responseLoginPackage);
    }
}
