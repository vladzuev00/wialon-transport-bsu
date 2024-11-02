package by.bsu.wialontransport.protocol.core.encoder.packages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageEncoder<PACKAGE> {
    private final Class<PACKAGE> responseType;

    public final boolean isAbleEncode(final Object response) {
        return responseType.isInstance(response);
    }

    public final byte[] encode(final Object response) {
        final PACKAGE castedPACKAGE = responseType.cast(response);
        return encodeInternal(castedPACKAGE);
    }

    protected abstract byte[] encodeInternal(final PACKAGE PACKAGE);
}
