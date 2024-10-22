package by.bsu.wialontransport.protocol.core.decoder.packages;

public abstract class FixStringPrefixedTextPackageDecoder extends PrefixedPackageDecoder<String, String> {

    public FixStringPrefixedTextPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final String content) {
        return content.substring(0, 1);
    }

    @Override
    protected int getLength(String s) {
        return 0;
    }

    @Override
    protected final String skip(final String content, int length) {
        return content.substring(2);
    }

    @Override
    protected Object decodeInternal(String s) {
        return null;
    }
}
