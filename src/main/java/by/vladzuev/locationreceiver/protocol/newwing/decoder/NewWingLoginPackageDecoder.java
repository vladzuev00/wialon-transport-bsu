package by.vladzuev.locationreceiver.protocol.newwing.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLoginPackage;
import by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingLoginPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String PREFIX = "GPRSGC";

    public NewWingLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected NewWingLoginPackage decodeInternal(final ByteBuf buffer) {
        final String imei = NewWingUtil.decodeImei(buffer);
        return new NewWingLoginPackage(imei);
    }
}
