package by.bsu.wialontransport.protocol.wialon.tempdecoder.chain;

import by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.RequestLoginPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage.PREFIX;

@Component
public final class RequestLoginPackageDecoder extends PackageDecoder {

    public RequestLoginPackageDecoder(final RequestPingPackageDecoder nextDecoder,
                                      final RequestLoginPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}