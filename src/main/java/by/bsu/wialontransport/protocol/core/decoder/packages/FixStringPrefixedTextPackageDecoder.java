package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class FixStringPrefixedTextPackageDecoder extends FixPrefixedPackageDecoder<String, String> {

    public FixStringPrefixedTextPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final String content) {
        return content.substring(0, 1);
    }

    @Override
    protected final String skip(final String content, int length) {
        return content.substring(2);
    }
}
