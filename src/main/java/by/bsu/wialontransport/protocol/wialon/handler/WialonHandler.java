package by.bsu.wialontransport.protocol.wialon.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnswerableException;
import by.bsu.wialontransport.protocol.wialon.handler.chain.StarterPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static java.lang.String.format;

//TODO: refactor and do test
@Slf4j
public final class WialonHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE
            = "Start handling inbound package: '%s'.";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected.";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '%s' is disconnected.";
    private static final String NOT_DEFINED_TRACKER_IMEI_IN_MESSAGE = "not defined imei";

    private final StarterPackageHandler starterPackageHandler;
    private final ContextAttributeManager contextAttributeManager;
    private final ConnectionManager connectionManager;

    public WialonHandler(final StarterPackageHandler starterPackageHandler,
                         final ContextAttributeManager contextAttributeManager,
                         final ConnectionManager connectionManager) {
        this.starterPackageHandler = starterPackageHandler;
        this.contextAttributeManager = contextAttributeManager;
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, final Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        log.info(format(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, requestPackage));
        this.starterPackageHandler.handle(requestPackage, context);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, Throwable exception) {
        if (exception instanceof DecoderException) {  //exception in decoders are wrapped in DecoderException
            exception = exception.getCause();
        }
        if (exception instanceof final AnswerableException answerableException) {
            context.writeAndFlush(answerableException.getAnswer());
        } else {
            context.fireExceptionCaught(exception);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        final Optional<String> optionalTrackerImei = this.contextAttributeManager.findTrackerImei(context);
        final String trackerImei = optionalTrackerImei.orElse(NOT_DEFINED_TRACKER_IMEI_IN_MESSAGE);
        log.info(format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, trackerImei));
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        optionalTracker.ifPresent(tracker -> this.connectionManager.remove(tracker.getId()));  //if device was authorized
    }
}
