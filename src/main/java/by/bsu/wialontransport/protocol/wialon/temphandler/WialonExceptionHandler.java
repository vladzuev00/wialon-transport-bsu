package by.bsu.wialontransport.protocol.wialon.temphandler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public final class WialonExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT = "During working with connection to tracker '{}' "
            + "was arisen exception: {}.";
    private static final String IMEI_NOT_DEFINED_TRACKER = "not defined imei";

    private final ContextAttributeManager contextAttributeManager;

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        this.logException(context, exception);
        exception.printStackTrace();
        context.close();
    }

    private void logException(final ChannelHandlerContext context, final Throwable exception) {
        final Optional<String> optionalTrackerImei = this.contextAttributeManager.findTrackerImei(context);
        final String trackerImei = optionalTrackerImei.orElse(IMEI_NOT_DEFINED_TRACKER);
        log.error(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, trackerImei, exception.getMessage());
    }
}