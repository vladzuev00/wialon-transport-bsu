package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

public final class TeltonikaRequestLocationPackageDecoder extends TeltonikaPackageDecoder {

    @Override
    public boolean isAbleDecode(final ByteBuf buffer) {
        return false;
    }

    @Override
    protected Object decodeInternal(final ByteBuf buffer) {
        return null;
    }
}
