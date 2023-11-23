package by.bsu.wialontransport.protocol.wialon.temphandler.chain;

import by.bsu.wialontransport.protocol.wialon.temphandler.chain.exception.NoSuitablePackageHandlerException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.channel.ChannelHandlerContext;

public abstract class PackageHandler {
    private final Class<? extends WialonPackage> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(final Class<? extends WialonPackage> packageType, final PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final void handle(final WialonPackage inboundPackage, final ChannelHandlerContext context) {
        if (this.isAbleToHandle(inboundPackage)) {
            this.handleIndependently(inboundPackage, context);
            return;
        }
        this.delegateToNextHandler(inboundPackage, context);
    }

    protected abstract void handleIndependently(final WialonPackage requestPackage, final ChannelHandlerContext context);

    private boolean isAbleToHandle(final WialonPackage requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private void delegateToNextHandler(final WialonPackage requestPackage, final ChannelHandlerContext context) {
        if (this.nextHandler == null) {
            throw new NoSuitablePackageHandlerException();
        }
        this.nextHandler.handle(requestPackage, context);
    }
}
