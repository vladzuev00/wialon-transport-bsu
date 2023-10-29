package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.core.service.receivingdata.ReceivingBlackBoxPackageService;
import by.bsu.wialontransport.protocol.wialon.handler.chain.FinisherPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage;

import org.springframework.stereotype.Component;

@Component
public final class RequestBlackBoxPackageHandler
        extends AbstractRequestDataPackageHandler<WialonRequestBlackBoxPackage> {

    public RequestBlackBoxPackageHandler(final FinisherPackageHandler nextHandler,
                                         final ReceivingBlackBoxPackageService receivingBlackBoxPackageService) {
        super(WialonRequestBlackBoxPackage.class, nextHandler, receivingBlackBoxPackageService);
    }
}
