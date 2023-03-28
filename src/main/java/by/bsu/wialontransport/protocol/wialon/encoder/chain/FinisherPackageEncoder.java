package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import org.springframework.stereotype.Component;

@Component
public final class FinisherPackageEncoder extends PackageEncoder {

    public FinisherPackageEncoder() {
        super(null, null);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final Package encodedPackage) {
        throw new UnsupportedOperationException();
    }
}
