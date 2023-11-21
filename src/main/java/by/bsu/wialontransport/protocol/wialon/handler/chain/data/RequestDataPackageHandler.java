package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;

import org.springframework.stereotype.Component;

@Component
public final class RequestDataPackageHandler
        extends AbstractRequestDataPackageHandler<WialonRequestDataPackage> {

    public RequestDataPackageHandler(final RequestBlackBoxPackageHandler nextHandler,
                                     final ReceivingDataPackageService receivingDataService) {
        super(WialonRequestDataPackage.class, nextHandler, receivingDataService);
    }
}
