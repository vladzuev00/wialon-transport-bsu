package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class FixStringPrefixedTextPackageDecoder extends FixStringPrefixedPackageDecoder<String> {

    public FixStringPrefixedTextPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final String content, final int length) {
        return content.substring(0, length);
    }
}
