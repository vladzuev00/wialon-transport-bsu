package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.newwing.util.NewWingUtil.decodeImei;

@Component
public final class NewWingLoginPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String REQUIRED_PREFIX = "GPRSGC";

    public NewWingLoginPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected Object decodeInternal(final ByteBuf buffer) {
        final String imei = decodeImei(buffer);
        return new NewWingLoginPackage(imei);
    }
}
