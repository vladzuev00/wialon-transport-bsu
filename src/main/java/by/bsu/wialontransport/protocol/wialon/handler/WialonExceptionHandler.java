package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WialonExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT = "During working with connection to tracker '{}' "
            + "was arisen exception: {}.";
    private static final String IMEI_NOT_DEFINED_TRACKER_IN_MESSAGE = "not defined imei";

    private final ContextAttributeManager contextAttributeManager;

    public WialonExceptionHandler(final ContextAttributeManager contextAttributeManager) {
        this.contextAttributeManager = contextAttributeManager;
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        final String imei = this.contextAttributeManager.findTrackerImei(context)
                .orElse(IMEI_NOT_DEFINED_TRACKER_IN_MESSAGE);
        log.error(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, imei, exception.getMessage());
        exception.printStackTrace();
        context.close();
    }
}