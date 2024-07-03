package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class FixStringPrefixedPackageDecoder<SOURCE> extends FixPrefixedPackageDecoder<SOURCE, String> {

    public FixStringPrefixedPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final SOURCE source) {
        final int length = getPrefix().length();
        return readPrefix(source, length);
    }

    protected abstract String readPrefix(final SOURCE source, final int length);
}
