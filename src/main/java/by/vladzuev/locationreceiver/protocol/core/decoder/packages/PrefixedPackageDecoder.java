package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PrefixedPackageDecoder<SOURCE, PREFIX> implements PackageDecoder<SOURCE> {
    private final PREFIX requiredPrefix;

    @Override
    public final boolean isAbleDecode(final SOURCE source) {
        final int prefixLength = getLength(requiredPrefix);
        final PREFIX prefix = readPrefix(source, prefixLength);
        return isEqual(prefix, requiredPrefix);
    }

    @Override
    public final Object decode(final SOURCE source) {
        final int prefixLength = getLength(requiredPrefix);
        final SOURCE unprefixedSource = skip(source, prefixLength);
        return decodeInternal(unprefixedSource);
    }

    protected abstract int getLength(final PREFIX prefix);

    protected abstract PREFIX readPrefix(final SOURCE source, final int length);

    protected abstract boolean isEqual(final PREFIX first, final PREFIX second);

    protected abstract SOURCE skip(final SOURCE source, final int length);

    protected abstract Object decodeInternal(final SOURCE source);
}
