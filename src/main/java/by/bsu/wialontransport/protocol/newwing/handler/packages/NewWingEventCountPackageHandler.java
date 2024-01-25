package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageHandler extends PackageHandler<NewWingEventCountPackage> {

    public NewWingEventCountPackageHandler() {
        super(NewWingEventCountPackage.class);
    }

    @Override
    protected Package handleInternal(final NewWingEventCountPackage request, final ChannelHandlerContext context) {
        return new NewWingSuccessResponsePackage();
    }
}
