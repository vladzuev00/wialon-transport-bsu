package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageHandler<PACKAGE extends Package> {
    private final Class<PACKAGE> handledPackageType;

    public final boolean isAbleToHandle(final Package requestPackage) {
        return handledPackageType.isInstance(requestPackage);
    }

    public final void handle(final Package requestPackage, final ChannelHandlerContext context) {
        final PACKAGE castedPackage = handledPackageType.cast(requestPackage);
        handleConcretePackage(castedPackage, context);
    }

    protected abstract void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context);
}
