package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.handler.chain.PackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractRequestDataPackage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractRequestDataPackageHandler<
        RequestPackageType extends AbstractRequestDataPackage,
        ResponsePackageType extends Package>
        extends PackageHandler {
    private final ReceivingDataService receivingDataService;

    public AbstractRequestDataPackageHandler(final Class<RequestPackageType> requestPackageType,
                                             final PackageHandler nextHandler,
                                             final ReceivingDataService receivingDataService) {
        super(requestPackageType, nextHandler);
        this.receivingDataService = receivingDataService;
    }

    @Override
    protected final void handleIndependently(final Package requestPackage, final ChannelHandlerContext context) {
        final List<Data> receivedData = this.findReceivedData(requestPackage);
        this.receivingDataService.receive(receivedData, context);
        //TODO: вынести в сервиc
        this.sendResponse(receivedData, context);
    }

    protected abstract ResponsePackageType createResponse(final int amountReceivedData);

    @SuppressWarnings("unchecked")
    private List<Data> findReceivedData(final Package requestPackage) {
        final RequestPackageType requestDataPackage = (RequestPackageType) requestPackage;
        return requestDataPackage.getData();
    }

    private void sendResponse(final List<Data> receivedData, final ChannelHandlerContext context) {
        final ResponsePackageType responsePackage = this.createResponse(receivedData.size());
        context.writeAndFlush(responsePackage);
    }
}
