package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PrefixedByStringBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

//TODO: test
@Component
public final class NewWingLoginPackageDecoder extends PrefixedByStringBinaryPackageDecoder {
    private static final String REQUIRED_PREFIX = "GPRSGC";

    public NewWingLoginPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected NewWingLoginPackage decodeInternal(final ByteBuf buffer) {
        final short imei = buffer.readShortLE();
        return new NewWingLoginPackage(imei);
    }
}
