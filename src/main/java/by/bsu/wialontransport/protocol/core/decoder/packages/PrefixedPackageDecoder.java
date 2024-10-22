package by.bsu.wialontransport.protocol.core.decoder.packages;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class PrefixedPackageDecoder<SOURCE, PREFIX> implements PackageDecoder<SOURCE> {
    private final PREFIX requiredPrefix;

    @Override
    public final boolean isAbleDecode(final SOURCE source) {
        final PREFIX prefix = readPrefix(source);
        return Objects.equals(prefix, requiredPrefix);
    }

    @Override
    public final Object decode(final SOURCE source) {
        final SOURCE sourceWithoutPrefix = skipPrefix(source);
        return decodeInternal(sourceWithoutPrefix);
    }

    protected abstract PREFIX readPrefix(final SOURCE source);

    protected abstract int getLength(final PREFIX prefix);

    protected abstract SOURCE skip(final SOURCE source, final int length);

    protected abstract Object decodeInternal(final SOURCE source);

    private SOURCE skipPrefix(final SOURCE source) {
        final int prefixLength = getLength(requiredPrefix);
        return skip(source, prefixLength);
    }
}
