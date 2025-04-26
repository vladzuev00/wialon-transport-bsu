package by.vladzuev.locationreceiver.protocol.core.handler;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.PackageHandler;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private final List<? extends PackageHandler<?>> packageHandlers;
    private final ContextAttributeManager contextAttributeManager;
    private final ContextManager contextManager;

    @Override
    public void channelRead(final ChannelHandlerContext context, final Object request) {
        logRequest(request);
        handle(request, context);
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        logActiveChannel();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        logInactiveChannel(context);
        remove(context);
    }

    private void logRequest(final Object request) {
        log.info("Start handling request: {}", request);
    }

    private void logActiveChannel() {
        log.info("New tracker is connected");
    }

    private void logInactiveChannel(final ChannelHandlerContext context) {
        contextAttributeManager.findImei(context).ifPresentOrElse(this::logInactiveChannel, this::logInactiveChannel);
    }

    private void logInactiveChannel(final String imei) {
        log.info("Tracker '{}' was disconnected", imei);
    }

    private void logInactiveChannel() {
        log.info("Unknown tracker was disconnected");
    }

    private void handle(final Object request, final ChannelHandlerContext context) {
        packageHandlers.stream()
                .filter(handler -> handler.isAbleHandle(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no suitable handler"))
                .handle(request, context);
    }

    private void remove(final ChannelHandlerContext context) {
        contextAttributeManager.findTracker(context)
                .map(Tracker::getId)
                .ifPresent(contextManager::remove);
    }
}
