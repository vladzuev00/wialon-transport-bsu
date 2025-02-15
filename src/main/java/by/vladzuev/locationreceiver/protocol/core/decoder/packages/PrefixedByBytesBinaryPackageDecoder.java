package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public abstract class PrefixedByBytesBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<byte[]> {

    public PrefixedByBytesBinaryPackageDecoder(final byte[] prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(final byte[] prefix) {
        return prefix.length;
    }

    @Override
    protected final byte[] readPrefix(final ByteBuf buffer, final int length) {
        final byte[] prefix = new byte[length];
        buffer.getBytes(0, prefix);
        return prefix;
    }

    @Override
    protected final boolean isEqual(final byte[] first, final byte[] second) {
        return Arrays.equals(first, second);
    }
}
