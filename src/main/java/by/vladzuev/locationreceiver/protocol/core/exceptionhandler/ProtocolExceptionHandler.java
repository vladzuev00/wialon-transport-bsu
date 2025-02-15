package by.vladzuev.locationreceiver.protocol.core.exceptionhandler;

import by.vladzuev.locationreceiver.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String NOT_DEFINED_TRACKER_IMEI = "imei-not-defined";

    private final ContextAttributeManager contextAttributeManager;

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        logException(context, exception);
        context.close();
    }

    private void logException(final ChannelHandlerContext context, final Throwable exception) {
        final String trackerImei = contextAttributeManager.findTrackerImei(context).orElse(NOT_DEFINED_TRACKER_IMEI);
        log.error("During working with tracker '{}' was arisen exception", trackerImei, exception);
    }
}
