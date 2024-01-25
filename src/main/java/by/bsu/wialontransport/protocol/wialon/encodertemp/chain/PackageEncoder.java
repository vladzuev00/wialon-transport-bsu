package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.encodertemp.chain.exception.NoSuitablePackageEncoderException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage.POSTFIX;

public abstract class PackageEncoder {
    private final Class<? extends WialonPackage> packageType;
    private final PackageEncoder nextEncoder;

    public PackageEncoder(final Class<? extends WialonPackage> packageType, final PackageEncoder nextEncoder) {
        this.packageType = packageType;
        this.nextEncoder = nextEncoder;
    }

    public final String encode(final WialonPackage encodedPackage) {
        return this.isAbleToEncode(encodedPackage)
                ? this.encodeIndependently(encodedPackage)
                : this.delegateToNextEncoder(encodedPackage);
    }

    protected abstract String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage);

    private String encodeIndependently(final WialonPackage encodedPackage) {
        return this.encodeIndependentlyWithoutPostfix(encodedPackage) + POSTFIX;
    }

    private boolean isAbleToEncode(final WialonPackage encodedPackage) {
        return this.packageType != null && this.packageType.isInstance(encodedPackage);
    }

    private String delegateToNextEncoder(final WialonPackage encodedPackage) {
        if (this.nextEncoder == null) {
            throw new NoSuitablePackageEncoderException();
        }
        return this.nextEncoder.encode(encodedPackage);
    }
}
