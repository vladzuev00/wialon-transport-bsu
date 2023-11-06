package by.bsu.wialontransport.protocol.wialon.tempdecoder.chain;

import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.RequestBlackBoxPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestBlackBoxPackage.PREFIX;

@Component
public final class RequestBlackBoxPackageDecoder extends PackageDecoder {

    public RequestBlackBoxPackageDecoder(final FinisherPackageDecoder nextDecoder,
                                         final RequestBlackBoxPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}
