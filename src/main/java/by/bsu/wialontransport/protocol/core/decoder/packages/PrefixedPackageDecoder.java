package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class PrefixedPackageDecoder<SOURCE, PREFIX> implements PackageDecoder<SOURCE> {

    @Override
    public final boolean isAbleDecode(final SOURCE source) {
        final PREFIX prefix = readPrefix(source);
        return isSuitablePrefix(prefix);
    }

    protected abstract PREFIX readPrefix(final SOURCE source);

    protected abstract boolean isSuitablePrefix(final PREFIX prefix);
}
