package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class PrefixedByBytesBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<byte[]> {

    public PrefixedByBytesBinaryPackageDecoder(final byte[] requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final int getLength(final byte[] requiredPrefix) {
        return requiredPrefix.length;
    }

    @Override
    protected final byte[] readPrefix(final ByteBuf buffer, final int length) {
        final byte[] prefix = new byte[length];
        buffer.getBytes(0, prefix);
        return prefix;
    }
}
