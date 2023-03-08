package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.core.service.receivingdata.ReceivingBlackBoxPackageService;
import by.bsu.wialontransport.protocol.wialon.handler.chain.FinisherPackageHandler;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

@Component
public final class RequestBlackBoxPackageHandler
        extends AbstractRequestDataPackageHandler<RequestBlackBoxPackage, ResponseBlackBoxPackage> {

    public RequestBlackBoxPackageHandler(final FinisherPackageHandler nextHandler,
                                         final ReceivingBlackBoxPackageService receivingBlackBoxPackageService) {
        super(RequestBlackBoxPackage.class, nextHandler, receivingBlackBoxPackageService);
    }
}
