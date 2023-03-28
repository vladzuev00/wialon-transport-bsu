package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.ResponsePingPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.ResponsePingPackage.PREFIX;

@Component
public final class ResponsePingPackageEncoder extends PackageEncoder {

    public ResponsePingPackageEncoder(final ResponseDataPackageEncoder nextEncoder) {
        super(ResponsePingPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final Package encodedPackage) {
        return PREFIX;
    }
}
