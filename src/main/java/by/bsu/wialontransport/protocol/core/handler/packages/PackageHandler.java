package by.bsu.wialontransport.protocol.core.handler.packages;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageHandler<REQUEST> {
    private final Class<REQUEST> requestType;

    public final boolean isAbleHandle(final Object request) {
        return requestType.isInstance(request);
    }

    public final void handle(final Object request, final ChannelHandlerContext context) {
        final REQUEST castedRequest = requestType.cast(request);
        final Object response = handleInternal(castedRequest, context);
        context.writeAndFlush(response);
    }

    protected abstract Object handleInternal(final REQUEST request, final ChannelHandlerContext context);
}
