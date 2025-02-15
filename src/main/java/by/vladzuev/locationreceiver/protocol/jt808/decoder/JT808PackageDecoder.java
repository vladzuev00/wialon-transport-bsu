package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedByBytesBinaryPackageDecoder;
import io.netty.buffer.ByteBuf;

import static by.vladzuev.locationreceiver.protocol.jt808.util.JT808Util.decodePhoneNumber;

public abstract class JT808PackageDecoder extends PrefixedByBytesBinaryPackageDecoder {

    public JT808PackageDecoder(final byte[] requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final Object decodeInternal(final ByteBuf buffer) {
        skipBodyProperties(buffer);
        final String phoneNumber = decodePhoneNumber(buffer);
        skipSerialNumber(buffer);
        return decodeInternal(buffer, phoneNumber);
    }

    protected abstract Object decodeInternal(final ByteBuf buffer, final String phoneNumber);

    private void skipBodyProperties(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipSerialNumber(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }
}
