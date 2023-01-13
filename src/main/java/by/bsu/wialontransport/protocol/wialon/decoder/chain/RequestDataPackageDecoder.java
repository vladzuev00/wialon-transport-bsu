package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.RequestDataPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.RequestDataPackage.PREFIX;

@Component
public final class RequestDataPackageDecoder extends PackageDecoder {

    public RequestDataPackageDecoder(final RequestBlackBoxPackageDecoder nextDecoder,
                                     final RequestDataPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}
