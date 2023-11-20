package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.EventCountNewWingPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.SuccessResponseNewWingPackage;
import io.netty.channel.ChannelHandlerContext;

public final class NewWingEventCountPackageHandler extends PackageHandler<EventCountNewWingPackage> {

    public NewWingEventCountPackageHandler() {
        super(EventCountNewWingPackage.class);
    }

    @Override
    protected void handleConcretePackage(final EventCountNewWingPackage requestPackage,
                                         final ChannelHandlerContext context) {
        final SuccessResponseNewWingPackage responsePackage = new SuccessResponseNewWingPackage();
        context.writeAndFlush(responsePackage);
    }
}
