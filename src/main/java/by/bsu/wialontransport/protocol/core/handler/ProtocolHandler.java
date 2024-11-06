package by.bsu.wialontransport.protocol.core.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private static final String MESSAGE_RECEIVE_REQUEST = "Start handling request: '{}'";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected";
    private static final String MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '{}' was disconnected";
    private static final String NOT_DEFINED_IMEI = "not defined imei";

    private final List<? extends PackageHandler<?>> packageHandlers;
    private final ContextAttributeManager contextAttributeManager;
    private final ChannelHandlerContextManager contextManager;

    @Override
    public void channelRead(final ChannelHandlerContext context, final Object request) {
        logReceivingRequest(request);
        handle(request, context);
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        logActiveChannel();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        logInactiveChannel(context);
        close(context);
    }

    private void logReceivingRequest(final Object request) {
        log.info(MESSAGE_RECEIVE_REQUEST, request);
    }

    private void logActiveChannel() {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    private void logInactiveChannel(final ChannelHandlerContext context) {
        final String trackerImei = contextAttributeManager.findTrackerImei(context).orElse(NOT_DEFINED_IMEI);
        log.info(MESSAGE_INACTIVE_CHANNEL, trackerImei);
    }

    private void handle(final Object request, final ChannelHandlerContext context) {
        packageHandlers.stream()
                .filter(handler -> handler.isAbleHandle(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler for request '%s'"))
                .handle(request, context);
    }

    private void close(final ChannelHandlerContext context) {
        contextAttributeManager.findTracker(context)
                .map(Tracker::getId)
                .ifPresent(contextManager::remove);
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
