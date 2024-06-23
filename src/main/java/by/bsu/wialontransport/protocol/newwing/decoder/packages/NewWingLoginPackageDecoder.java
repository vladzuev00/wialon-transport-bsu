package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class NewWingLoginPackageDecoder extends NewWingPackageDecoder {
    private static final String PREFIX = "GPRSGC";
    private static final String TEMPLATE_IMEI = "%020d";

    public NewWingLoginPackageDecoder() {
        super(PREFIX);
    }

    //TODO: заполнение дополнительными нулями сделать в handler
    @Override
    protected PackageFactory decodeUntilChecksum(final ByteBuf buffer) {
        final String imei = decodeImei(buffer);
        return checksum -> new NewWingLoginPackage(checksum, imei);
    }

    private String decodeImei(final ByteBuf buffer) {
        final short value = buffer.readShortLE();
        return format(TEMPLATE_IMEI, value);
    }
}
