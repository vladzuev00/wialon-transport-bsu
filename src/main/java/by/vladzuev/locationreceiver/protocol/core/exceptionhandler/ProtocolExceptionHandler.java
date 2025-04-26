package by.vladzuev.locationreceiver.protocol.core.exceptionhandler;

import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolExceptionHandler extends ChannelInboundHandlerAdapter {
    private final ContextAttributeManager contextAttributeManager;

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        try {
            logException(context, exception);
        } finally {
            context.close();
        }
    }

    private void logException(final ChannelHandlerContext context, final Throwable exception) {
        contextAttributeManager.findTrackerImei(context)
                .ifPresentOrElse(
                        trackerImei -> logException(trackerImei, exception),
                        () -> logException(exception)
                );
    }

    private void logException(final String trackerImei, final Throwable exception) {
        log.error("Communication with tracker '{}' arose exception", trackerImei, exception);
    }

    private void logException(final Throwable exception) {
        log.error("Communication with unknown tracker arose exception", exception);
    }
}
