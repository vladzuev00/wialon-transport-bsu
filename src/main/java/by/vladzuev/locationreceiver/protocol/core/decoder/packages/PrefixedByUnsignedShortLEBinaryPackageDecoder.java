package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

public abstract class PrefixedByUnsignedShortLEBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<Integer> {

    public PrefixedByUnsignedShortLEBinaryPackageDecoder(final Integer prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(final Integer prefix) {
        return Short.BYTES;
    }

    @Override
    protected final Integer readPrefix(final ByteBuf buffer, final int length) {
        return buffer.getUnsignedShortLE(0);
    }

    @Override
    protected final boolean isEqual(final Integer first, final Integer second) {
        return Objects.equals(first, second);
    }
}
