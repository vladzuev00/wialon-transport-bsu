package by.bsu.wialontransport.protocol.core.handler.packages.ping;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import io.netty.channel.ChannelHandlerContext;

public abstract class PingPackageHandler<PACKAGE> extends PackageHandler<PACKAGE> {

    public PingPackageHandler(final Class<PACKAGE> requestType) {
        super(requestType);
    }

    @Override
    protected final Object handleInternal(final PACKAGE request, final ChannelHandlerContext context) {
        return createResponse();
    }

    protected abstract Object createResponse();
}
