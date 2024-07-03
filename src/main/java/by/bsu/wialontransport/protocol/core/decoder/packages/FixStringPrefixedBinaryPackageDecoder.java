package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class FixStringPrefixedBinaryPackageDecoder extends FixPrefixedBinaryPackageDecoder<String> {

    public FixStringPrefixedBinaryPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final void skipPrefix(final ByteBuf buffer) {
        buffer.skipBytes(getPrefix().length());
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer) {
        return buffer.getCharSequence(0, getPrefix().length(), US_ASCII).toString();
    }
}
