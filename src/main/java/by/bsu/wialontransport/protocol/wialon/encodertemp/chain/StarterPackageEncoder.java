package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import org.springframework.stereotype.Component;

@Component
public final class StarterPackageEncoder extends PackageEncoder {

    public StarterPackageEncoder(final ResponseLoginPackageEncoder nextEncoder) {
        super(null, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        throw new UnsupportedOperationException();
    }
}
