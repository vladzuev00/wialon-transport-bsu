package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.RequestLoginPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage.PREFIX;

@Component
public final class RequestLoginPackageDecoder extends PackageDecoder {

    public RequestLoginPackageDecoder(final RequestPingPackageDecoder nextDecoder,
                                      final RequestLoginPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}