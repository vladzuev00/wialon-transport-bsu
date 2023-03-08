package by.bsu.wialontransport.protocol.wialon.handler.chain.data;

import by.bsu.wialontransport.protocol.core.service.receivingdata.ReceivingDataPackageService;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;
import org.springframework.stereotype.Component;

@Component
public final class RequestDataPackageHandler
        extends AbstractRequestDataPackageHandler<RequestDataPackage, ResponseDataPackage> {

    public RequestDataPackageHandler(final RequestBlackBoxPackageHandler nextHandler,
                                     final ReceivingDataPackageService receivingDataService) {
        super(RequestDataPackage.class, nextHandler, receivingDataService);
    }
}
