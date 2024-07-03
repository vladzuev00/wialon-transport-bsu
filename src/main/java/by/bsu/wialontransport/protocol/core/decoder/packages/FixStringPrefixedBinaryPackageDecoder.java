package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class FixStringPrefixedBinaryPackageDecoder extends FixStringPrefixedPackageDecoder<ByteBuf> {

    public FixStringPrefixedBinaryPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer, final int length) {
        return buffer.getCharSequence(0, length, US_ASCII).toString();
    }
}
