package by.bsu.wialontransport.protocol.wialon.decoder.chain;

import by.bsu.wialontransport.protocol.wialon.decoder.deserializer.RequestPingPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage.PREFIX;

@Component
public final class RequestPingPackageDecoder extends PackageDecoder {

    public RequestPingPackageDecoder(final RequestDataPackageDecoder nextDecoder,
                                     final RequestPingPackageDeserializer packageDeserializer) {
        super(nextDecoder, PREFIX, packageDeserializer);
    }
}
