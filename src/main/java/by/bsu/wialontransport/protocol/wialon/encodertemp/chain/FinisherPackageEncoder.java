package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import org.springframework.stereotype.Component;

@Component
public final class FinisherPackageEncoder extends PackageEncoder {

    public FinisherPackageEncoder() {
        super(null, null);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        throw new UnsupportedOperationException();
    }
}
