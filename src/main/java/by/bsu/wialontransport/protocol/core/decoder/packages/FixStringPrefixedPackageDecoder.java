package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class FixStringPrefixedPackageDecoder extends FixPrefixedPackageDecoder<String> {

    public FixStringPrefixedPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final String readPrefix(final ByteBuf buffer) {
        final int length = getPrefix().length();
        return buffer.readCharSequence(length, UTF_8).toString();
    }
}
