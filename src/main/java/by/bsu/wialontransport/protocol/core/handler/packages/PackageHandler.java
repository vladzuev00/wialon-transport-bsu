package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageHandler<REQUEST_TYPE extends Package, RESPONSE extends Package> {
    private final Class<REQUEST_TYPE> handledPackageType;

    public final boolean isAbleToHandle(final Package request) {
        return handledPackageType.isInstance(request);
    }

    public final void handle(final Package request, final ChannelHandlerContext context) {
        final REQUEST_TYPE castedRequest = handledPackageType.cast(request);
        final Package response = handleInternal(castedRequest, context);
        context.writeAndFlush(response);
    }

    protected abstract Package handleInternal(final REQUEST_TYPE request, final ChannelHandlerContext context);
}
