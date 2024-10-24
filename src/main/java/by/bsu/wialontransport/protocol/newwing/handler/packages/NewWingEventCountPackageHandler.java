package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageHandler extends PackageHandler {

    public NewWingEventCountPackageHandler() {
        super(NewWingEventCountPackage.class);
    }

//    @Override
//    protected Package handleInternal(final NewWingEventCountPackage request, final ChannelHandlerContext context) {
//        return new NewWingSuccessResponsePackage();
//    }

    @Override
    protected Package handleInternal(Package request, ChannelHandlerContext context) {
        return null;
    }
}
