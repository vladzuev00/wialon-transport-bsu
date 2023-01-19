package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.handler.chain.exception.NoSuitablePackageHandlerException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;

public abstract class PackageHandler {
    private final Class<? extends Package> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(final Class<? extends Package> packageType, final PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final void handle(final Package inboundPackage, final ChannelHandlerContext context) {
        if (this.isAbleToHandle(inboundPackage)) {
            this.handleIndependently(inboundPackage, context);
            return;
        }
        this.delegateToNextHandler(inboundPackage, context);
    }

    protected abstract void handleIndependently(final Package requestPackage, final ChannelHandlerContext context);

    private boolean isAbleToHandle(final Package requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private void delegateToNextHandler(final Package requestPackage, final ChannelHandlerContext context) {
        if (this.nextHandler == null) {
            throw new NoSuitablePackageHandlerException();
        }
        this.nextHandler.handle(requestPackage, context);
    }
}
