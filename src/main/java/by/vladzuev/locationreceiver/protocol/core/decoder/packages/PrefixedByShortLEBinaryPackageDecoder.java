package by.vladzuev.locationreceiver.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

public abstract class PrefixedByShortLEBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<Short> {

    public PrefixedByShortLEBinaryPackageDecoder(final Short prefix) {
        super(prefix);
    }

    @Override
    protected final int getLength(final Short prefix) {
        return Short.BYTES;
    }

    @Override
    protected final Short readPrefix(final ByteBuf buffer, final int length) {
        return buffer.readShortLE();
    }

    @Override
    protected final boolean isEqual(final Short first, final Short second) {
        return Objects.equals(first, second);
    }
}
