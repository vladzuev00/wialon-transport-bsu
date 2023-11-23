package by.bsu.wialontransport.protocol.wialon.temphandler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.wialon.temphandler.chain.StarterPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public final class WialonHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE
            = "Start handling inbound package: '%s'.";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected.";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '%s' is disconnected.";
    private static final String NOT_DEFINED_TRACKER_IMEI = "not defined imei";

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
        final WialonPackage requestPackage = (WialonPackage) requestObject;
        log.info(format(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, requestPackage));
        this.starterPackageHandler.handle(requestPackage, context);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, Throwable exception) {
        if (exception instanceof DecoderException) {  //exceptions in decoders are wrapped in DecoderException
            exception = exception.getCause();
        }
        if (exception instanceof final AnsweredException answerableException) {
            context.writeAndFlush(answerableException.getAnswer());
        } else {
            context.fireExceptionCaught(exception);
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        this.logAboutInactiveChannel(context);
        this.removeConnectionInfoIfTrackerWasAuthorized(context);
    }

    private void logAboutInactiveChannel(final ChannelHandlerContext context) {
        final String trackerImei = this.findTrackerImei(context);
        log.info(format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, trackerImei));
    }

    private String findTrackerImei(final ChannelHandlerContext context) {
        final Optional<String> optionalTrackerImei = this.contextAttributeManager.findTrackerImei(context);
        return optionalTrackerImei.orElse(NOT_DEFINED_TRACKER_IMEI);
    }

    private void removeConnectionInfoIfTrackerWasAuthorized(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        optionalTracker.ifPresent(tracker -> this.connectionManager.remove(tracker.getId()));
    }
}
