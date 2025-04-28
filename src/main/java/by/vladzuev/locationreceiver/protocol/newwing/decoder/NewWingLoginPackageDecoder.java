package by.vladzuev.locationreceiver.protocol.newwing.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil.decodeImei;

@Component
public final class NewWingLoginPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String PREFIX = "GPRSGC";

    public NewWingLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected NewWingLoginPackage decodeInternal(final ByteBuf buffer) {
        final String imei = decodeImei(buffer);
        return new NewWingLoginPackage(imei);
    }
}
