package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class PrefixedByStringBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<String> {

    public PrefixedByStringBinaryPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(final String prefix) {
        return prefix.length();
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer, final int length) {
        return buffer.getCharSequence(0, length, US_ASCII).toString();
    }

    @Override
    protected final boolean isEqual(final String first, final String second) {
        return Objects.equals(first, second);
    }
}
