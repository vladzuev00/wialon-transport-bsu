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

    public final void handle(final Package request, final ChannelHandlerContext context) {
        final PACKAGE castedRequest = handledPackageType.cast(request);
        final Package response = handleInternal(castedRequest, context);
        context.writeAndFlush(response);
    }

    protected abstract Package handleInternal(final PACKAGE request, final ChannelHandlerContext context);
}
