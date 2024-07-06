package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class FixStringPrefixedTextPackageDecoder extends FixPrefixedPackageDecoder<String, String> {

    public FixStringPrefixedTextPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final String content) {
        return content.substring(0, getPrefix().length());
    }

    @Override
    protected final String removePrefix(final String content) {
        return content.substring(getPrefix().length());
    }
}
