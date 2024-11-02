package by.bsu.wialontransport.protocol.core.encoder.packages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PackageEncoder<PACKAGE> {
    private final Class<PACKAGE> responseType;

    public final boolean isAbleEncode(final Object response) {
        return responseType.isInstance(response);
    }

    public final byte[] encode(final Object response) {
        final PACKAGE castedResponse = responseType.cast(response);
        return encodeInternal(castedResponse);
    }

    protected abstract byte[] encodeInternal(final PACKAGE response);
}
