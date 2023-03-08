package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.RequestBlackBoxPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.RequestBlackBoxPackage.PREFIX;

@Component
public final class RequestBlackBoxPackageDecoder extends PackageDecoder {

    public RequestBlackBoxPackageDecoder(final FinisherPackageDecoder nextDecoder,
                                         final RequestBlackBoxPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}
