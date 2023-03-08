package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.wialon.handler.chain.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class RequestDataPackageHandler extends PackageHandler {
    private final ReceivingDataService receivingDataService;

    public RequestDataPackageHandler(final ReceivingDataService receivingDataService) {
        super(RequestDataPackage.class, null);
        this.receivingDataService = receivingDataService;
    }

    @Override
    protected void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        final RequestDataPackage requestDataPackage = (RequestDataPackage) requestPackage;
        this.receivingDataService.receive(requestDataPackage, context);
    }
}
