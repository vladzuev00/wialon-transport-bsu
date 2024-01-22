package by.bsu.wialontransport.protocol.core.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE = "Start handling request package: '{}'";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '{}' was disconnected";
    private static final String NOT_DEFINED_TRACKER_IMEI = "not defined imei";

    private final List<PackageHandler<?>> packageHandlers;
    private final ContextAttributeManager contextAttributeManager;
    private final ConnectionManager connectionManager;

    @Override
    public final void channelRead(final ChannelHandlerContext context, final Object requestObject) {
        final Package request = (Package) requestObject;
        logStartHandlingPackage(request);
        final PackageHandler<?> packageHandler = findPackageHandler(request);
        packageHandler.handle(request, context);
    }

    @Override
    public final void channelActive(final ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public final void channelInactive(final ChannelHandlerContext context) {
        logAboutInactiveChannel(context);
        removeConnectionInfoIfTrackerWasAuthorized(context);
    }

    private static void logStartHandlingPackage(final Package request) {
        log.info(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, request);
    }

    private PackageHandler<?> findPackageHandler(final Package requestPackage) {
        return packageHandlers.stream()
                .filter(packageHandler -> packageHandler.isAbleToHandle(requestPackage))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageHandlerException(
                                "No package handler for package: %s".formatted(requestPackage)
                        )
                );
    }

    private void logAboutInactiveChannel(final ChannelHandlerContext context) {
        final String trackerImei = contextAttributeManager.findTrackerImei(context).orElse(NOT_DEFINED_TRACKER_IMEI);
        log.info(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, trackerImei);
    }

    private void removeConnectionInfoIfTrackerWasAuthorized(final ChannelHandlerContext context) {
        contextAttributeManager.findTracker(context)
                .map(Tracker::getId)
                .ifPresent(connectionManager::remove);
    }

    static final class NoSuitablePackageHandlerException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoSuitablePackageHandlerException() {

        }

        public NoSuitablePackageHandlerException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageHandlerException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoSuitablePackageHandlerException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
