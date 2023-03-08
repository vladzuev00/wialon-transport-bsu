package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import org.springframework.stereotype.Component;

@Component
public final class StarterPackageEncoder extends PackageEncoder {

    public StarterPackageEncoder(final ResponseLoginPackageEncoder nextEncoder) {
        super(null, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final Package encodedPackage) {
        throw new UnsupportedOperationException();
    }
}
