package by.bsu.wialontransport.protocol.core.encoder.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageEncoder<PACKAGE> {
    private final Class<PACKAGE> encodedPackageType;

    public final boolean isAbleToEncode(final Package response) {
        return encodedPackageType.isInstance(response);
    }

    public final String encode(final Package response) {
        final PACKAGE castedResponse = encodedPackageType.cast(response);
        return encodeInternal(castedResponse);
    }

    protected abstract String encodeInternal(final PACKAGE response);
}
