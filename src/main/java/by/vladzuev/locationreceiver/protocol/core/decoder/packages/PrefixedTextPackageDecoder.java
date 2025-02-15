package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import java.util.Objects;

public abstract class PrefixedTextPackageDecoder extends PrefixedPackageDecoder<String, String> {

    public PrefixedTextPackageDecoder(final String requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final String readPrefix(final String source, final int length) {
        return source.substring(0, length);
    }

    @Override
    protected final boolean isEqual(final String first, final String second) {
        return Objects.equals(first, second);
    }

    @Override
    protected final int getLength(final String prefix) {
        return prefix.length();
    }

    @Override
    protected final String skip(final String source, final int length) {
        return source.substring(length);
    }
}
