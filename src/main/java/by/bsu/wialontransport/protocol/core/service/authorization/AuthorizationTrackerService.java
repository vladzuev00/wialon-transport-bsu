package by.bsu.wialontransport.protocol.core.service.authorization;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status.*;

//TODO: refactor tests
@Service
@RequiredArgsConstructor
public final class AuthorizationTrackerService {
    private final ContextAttributeManager contextAttributeManager;
    private final TrackerService trackerService;
    private final ConnectionManager connectionManager;
    private final DataService dataService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void authorize(final WialonRequestLoginPackage requestPackage, final ChannelHandlerContext context) {
        this.contextAttributeManager.putTrackerImei(context, requestPackage.getImei());
        final Optional<Tracker> optionalTracker = this.trackerService.findByImei(requestPackage.getImei());
        final Status status = optionalTracker
                .map(tracker -> this.checkPassword(tracker, requestPackage.getPassword()))
                .orElse(CONNECTION_FAILURE);
        if (status == SUCCESS_AUTHORIZATION) {
            final Tracker tracker = optionalTracker.get();
            this.contextAttributeManager.putTracker(context, tracker);
            this.connectionManager.add(context);
            this.putLastDataIfExist(context, tracker);
        }
        sendResponse(context, status);
    }

    private Status checkPassword(final Tracker tracker, final String packagePassword) {
        final String deviceEncryptedPassword = tracker.getPassword();
        return this.passwordEncoder.matches(packagePassword, deviceEncryptedPassword)
                ? SUCCESS_AUTHORIZATION
                : ERROR_CHECK_PASSWORD;
    }

    private void putLastDataIfExist(final ChannelHandlerContext context, final Tracker tracker) {
        final Optional<Data> optionalLastData = this.dataService.findTrackerLastData(tracker);
        optionalLastData.ifPresent(data -> this.contextAttributeManager.putLastData(context, data));
    }

    private static void sendResponse(final ChannelHandlerContext context, final Status status) {
        final WialonResponseLoginPackage responseLoginPackage = new WialonResponseLoginPackage(status);
        context.writeAndFlush(responseLoginPackage);
    }
}
