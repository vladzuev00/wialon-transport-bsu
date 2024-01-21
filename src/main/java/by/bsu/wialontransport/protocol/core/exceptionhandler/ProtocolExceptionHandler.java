package by.bsu.wialontransport.protocol.core.exceptionhandler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT
            = "During working with tracker '{}' was arisen exception: {}.";
    private static final String IMEI_NOT_DEFINED_TRACKER = "not defined imei";

    private final ContextAttributeManager contextAttributeManager;

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        logException(context, exception);
        exception.printStackTrace();
        context.close();
    }

    private void logException(final ChannelHandlerContext context, final Throwable exception) {
        final String trackerImei = contextAttributeManager.findTrackerImei(context).orElse(IMEI_NOT_DEFINED_TRACKER);
        log.error(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, trackerImei, exception.getMessage());
    }
}
