package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;
import io.netty.channel.ChannelHandlerContext;

public final class NewWingEventCountPackageHandler extends PackageHandler<NewWingEventCountPackage> {

    public NewWingEventCountPackageHandler() {
        super(NewWingEventCountPackage.class);
    }

    @Override
    protected void handleConcretePackage(final NewWingEventCountPackage requestPackage,
                                         final ChannelHandlerContext context) {
        final NewWingSuccessResponsePackage responsePackage = new NewWingSuccessResponsePackage();
        context.writeAndFlush(responsePackage);
    }
}
