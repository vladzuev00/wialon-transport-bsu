package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class NewWingLoginPackageDecoder extends NewWingPackageDecoder {
    private static final String PREFIX = "GPRSGC";

    public NewWingLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
        final String imei = readImei(buffer);
        return checksum -> new NewWingLoginPackage(checksum, imei);
    }

    private String readImei(final ByteBuf buffer) {
        final short imei = buffer.readShortLE();
        return Short.toString(imei);
    }

    @Override
    protected int getLength(String s) {
        return 0;
    }

    @Override
    protected String readPrefix(ByteBuf buf, int length) {
        return null;
    }
}
