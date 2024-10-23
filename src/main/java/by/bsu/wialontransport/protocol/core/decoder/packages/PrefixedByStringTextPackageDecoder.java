package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class PrefixedByStringTextPackageDecoder extends PrefixedPackageDecoder<String, String> {

    public PrefixedByStringTextPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final String source, final int length) {
        return source.substring(0, length);
    }

    @Override
    protected final int getLength(final String requiredPrefix) {
        return requiredPrefix.length();
    }

    @Override
    protected final String skip(final String source, final int length) {
        return source.substring(length);
    }
}
