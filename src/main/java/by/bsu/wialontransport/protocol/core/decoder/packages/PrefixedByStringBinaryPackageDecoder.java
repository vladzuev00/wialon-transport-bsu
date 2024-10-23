package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class PrefixedByStringBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<String> {

    public PrefixedByStringBinaryPackageDecoder(final String requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final int getLength(final String requiredPrefix) {
        return requiredPrefix.length();
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer, final int length) {
        return buffer.getCharSequence(0, length, US_ASCII).toString();
    }
}
