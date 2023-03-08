package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.encoder.chain.exception.NoSuitablePackageEncoderException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.Package.POSTFIX;

public abstract class PackageEncoder {
    private final Class<? extends Package> packageType;
    private final PackageEncoder nextEncoder;

    public PackageEncoder(final Class<? extends Package> packageType, final PackageEncoder nextEncoder) {
        this.packageType = packageType;
        this.nextEncoder = nextEncoder;
    }

    public final String encode(final Package encodedPackage) {
        final String encodedWithoutPostfix = this.isAbleToEncode(encodedPackage)
                ? this.encodeIndependentlyWithoutPostfix(encodedPackage)
                : this.delegateToNextEncoder(encodedPackage);
        return encodedWithoutPostfix + POSTFIX;
    }

    protected abstract String encodeIndependentlyWithoutPostfix(final Package encodedPackage);

    private boolean isAbleToEncode(final Package encodedPackage) {
        return this.packageType != null && this.packageType.isInstance(encodedPackage);
    }

    private String delegateToNextEncoder(final Package encodedPackage) {
        if (this.nextEncoder == null) {
            throw new NoSuitablePackageEncoderException();
        }
        return this.nextEncoder.encode(encodedPackage);
    }
}
