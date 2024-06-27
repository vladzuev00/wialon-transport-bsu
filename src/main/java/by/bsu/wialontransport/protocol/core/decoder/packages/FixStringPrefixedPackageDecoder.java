package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class FixStringPrefixedPackageDecoder extends FixPrefixedPackageDecoder {

    public FixStringPrefixedPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer) {
//        final int length = getPrefix().length();
        return buffer.readCharSequence(3, US_ASCII).toString();
    }
}
