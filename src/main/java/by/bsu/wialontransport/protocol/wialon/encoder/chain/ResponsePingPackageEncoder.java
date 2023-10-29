package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage.PREFIX;

@Component
public final class ResponsePingPackageEncoder extends PackageEncoder {

    public ResponsePingPackageEncoder(final ResponseDataPackageEncoder nextEncoder) {
        super(WialonResponsePingPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        return PREFIX;
    }
}
