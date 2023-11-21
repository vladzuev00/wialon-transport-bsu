package by.bsu.wialontransport.protocol.wialon.temphandler.chain.data;

import by.bsu.wialontransport.protocol.core.service.receivingdata.ReceivingDataService;
import by.bsu.wialontransport.protocol.wialon.temphandler.chain.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractWialonRequestDataPackage;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractRequestDataPackageHandler<RequestPackageType extends AbstractWialonRequestDataPackage>
        extends PackageHandler {
    private final ReceivingDataService<RequestPackageType, ?> receivingPackageService;

    public AbstractRequestDataPackageHandler(final Class<RequestPackageType> requestPackageType,
                                             final PackageHandler nextHandler,
                                             final ReceivingDataService<RequestPackageType, ?> receivingPackageService) {
        super(requestPackageType, nextHandler);
        this.receivingPackageService = receivingPackageService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void handleIndependently(final WialonPackage requestPackage, final ChannelHandlerContext context) {
        final RequestPackageType requestDataPackage = (RequestPackageType) requestPackage;
        this.receivingPackageService.receive(requestDataPackage, context);
    }
}
