package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class FinisherPackageHandler extends PackageHandler {

    public FinisherPackageHandler() {
        super(null, null);
    }

    @Override
    protected void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        throw new UnsupportedOperationException();
    }
}
