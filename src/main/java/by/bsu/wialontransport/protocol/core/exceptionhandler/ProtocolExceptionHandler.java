package by.bsu.wialontransport.protocol.core.exceptionhandler;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ProtocolExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT = "During working with tracker '{}' was arisen exception: {}.";
    private static final String NOT_DEFINED_TRACKER_IMEI = "not defined";

    private final ContextAttributeManager contextAttributeManager;

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final Throwable exception) {
        if (unwrapIfDecoderException(exception) instanceof final AnsweredException answeredException) {
            context.writeAndFlush(answeredException.getAnswer());
        } else {
            logException(context, exception);
            exception.printStackTrace();
            context.close();
        }
    }

    private static Throwable unwrapIfDecoderException(final Throwable exception) {
        return (exception instanceof DecoderException) ? exception.getCause() : exception;
    }

    private void logException(final ChannelHandlerContext context, final Throwable exception) {
        final String trackerImei = contextAttributeManager.findTrackerImei(context).orElse(NOT_DEFINED_TRACKER_IMEI);
        log.error(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, trackerImei, exception.getMessage());
    }
}
