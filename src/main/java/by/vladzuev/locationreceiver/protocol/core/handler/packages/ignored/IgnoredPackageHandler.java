package by.vladzuev.locationreceiver.protocol.core.handler.packages.ignored;

import by.vladzuev.locationreceiver.protocol.core.handler.packages.PackageHandler;
import io.netty.channel.ChannelHandlerContext;

public abstract class IgnoredPackageHandler<REQUEST> extends PackageHandler<REQUEST> {

    public IgnoredPackageHandler(final Class<REQUEST> requestType) {
        super(requestType);
    }

    @Override
    protected final Object handleInternal(final REQUEST request, final ChannelHandlerContext context) {
        return createResponse();
    }

    protected abstract Object createResponse();
}
