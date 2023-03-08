package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.core.service.receivingdata.AbstractReceivingDataPackageService;
import by.bsu.wialontransport.protocol.wialon.handler.chain.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractRequestDataPackage;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractRequestDataPackageHandler<
        RequestPackageType extends AbstractRequestDataPackage,
        ResponsePackageType extends Package>
        extends PackageHandler {
    private final AbstractReceivingDataPackageService<RequestPackageType, ResponsePackageType> receivingPackageService;

    public AbstractRequestDataPackageHandler(final Class<RequestPackageType> requestPackageType,
                                             final PackageHandler nextHandler,
                                             final AbstractReceivingDataPackageService<RequestPackageType, ResponsePackageType> receivingPackageService) {
        super(requestPackageType, nextHandler);
        this.receivingPackageService = receivingPackageService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        final RequestPackageType requestDataPackage = (RequestPackageType) requestPackage;
        this.receivingPackageService.receive(requestDataPackage, context);
    }
}
