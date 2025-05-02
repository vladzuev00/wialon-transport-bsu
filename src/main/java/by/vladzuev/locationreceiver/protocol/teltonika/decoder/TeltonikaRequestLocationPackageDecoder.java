package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import io.netty.buffer.ByteBuf;

public final class TeltonikaRequestLocationPackageDecoder extends TeltonikaPackageDecoder {

    public TeltonikaRequestLocationPackageDecoder(final LoginSuccessHolder loginSuccessHolder) {
        super(loginSuccessHolder, true);
    }

    @Override
    protected Object decodeInternal(final ByteBuf buffer) {
        return null;
    }
}
