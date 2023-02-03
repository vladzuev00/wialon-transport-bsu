package by.bsu.wialontransport.protocol.wialon.handler.chain;

import by.bsu.wialontransport.protocol.core.service.receivingdatapackage.ReceivingDataPackageService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.RequestDataPackage;
import io.netty.channel.ChannelHandlerContext;

public final class RequestDataPackageHandler extends PackageHandler {
    private final ReceivingDataPackageService receivingDataPackageService;

    public RequestDataPackageHandler(final ReceivingDataPackageService receivingDataPackageService) {
        super(RequestDataPackage.class, null);
        this.receivingDataPackageService = receivingDataPackageService;
    }

    @Override
    protected void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        final RequestDataPackage requestDataPackage = (RequestDataPackage) requestPackage;
        this.receivingDataPackageService.receive(requestDataPackage, context);
    }
}
