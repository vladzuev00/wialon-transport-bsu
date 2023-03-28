package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import org.springframework.stereotype.Component;

@Component
public final class ResponseBlackBoxPackageEncoder extends PackageEncoder {

    public ResponseBlackBoxPackageEncoder(Class<? extends Package> packageType, PackageEncoder nextEncoder) {
        super(packageType, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(Package encodedPackage) {
        return null;
    }
}
