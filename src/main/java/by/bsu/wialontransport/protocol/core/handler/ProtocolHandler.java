package by.bsu.wialontransport.protocol.core.handler;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exception.AnsweredException;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class ProtocolHandler<PACKAGE_HANDLER extends PackageHandler<?>> extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE = "Start handling request package: '{}'";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker with imei '{}' was disconnected";
    private static final String ALIAS_NOT_DEFINED_TRACKER_IMEI = "not defined imei";

    private final List<PACKAGE_HANDLER> packageHandlers;
    private final ContextAttributeManager contextAttributeManager;
    private final ConnectionManager connectionManager;

    @Override
    public final void channelRead(final ChannelHandlerContext context, final Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        logStartHandlingPackage(requestPackage);
        final PACKAGE_HANDLER packageHandler = this.findPackageHandler(requestPackage);
        packageHandler.handle(requestPackage, context);
    }

    @Override
    public final void exceptionCaught(final ChannelHandlerContext context, final Throwable caughtException) {
        final Throwable exception = unwrapIfDecoderException(caughtException);
        if (exception instanceof final AnsweredException answeredException) {
            sendAnswer(context, answeredException);
        } else {
            context.fireExceptionCaught(exception);
        }
    }

    @Override
    public final void channelActive(final ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public final void channelInactive(final ChannelHandlerContext context) {
        this.logAboutInactiveChannel(context);
        this.removeConnectionInfoIfTrackerWasAuthorized(context);
    }

    private static void logStartHandlingPackage(final Package requestPackage) {
        log.info(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, requestPackage);
    }

    private PACKAGE_HANDLER findPackageHandler(final Package requestPackage) {
        return this.packageHandlers.stream()
                .filter(packageHandler -> packageHandler.isAbleToHandle(requestPackage))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuitablePackageHandlerException(
                                "No package handler for package: %s".formatted(requestPackage)
                        )
                );
    }

    private static Throwable unwrapIfDecoderException(final Throwable exception) {
        return (exception instanceof DecoderException) ? exception.getCause() : exception;
    }

    private static void sendAnswer(final ChannelHandlerContext context, final AnsweredException exception) {
        final Package answer = exception.getAnswer();
        context.writeAndFlush(answer);
    }

    private void logAboutInactiveChannel(final ChannelHandlerContext context) {
        final Optional<String> optionalTrackerImei = this.contextAttributeManager.findTrackerImei(context);
        final String trackerImei = optionalTrackerImei.orElse(ALIAS_NOT_DEFINED_TRACKER_IMEI);
        log.info(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, trackerImei);
    }

    private void removeConnectionInfoIfTrackerWasAuthorized(final ChannelHandlerContext context) {
        this.contextAttributeManager.findTracker(context)
                .map(Tracker::getId)
                .ifPresent(this.connectionManager::remove);
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
