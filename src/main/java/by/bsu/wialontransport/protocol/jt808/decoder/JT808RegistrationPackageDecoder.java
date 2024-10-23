package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808RegistrationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.jt808.JT808Util.decodeString;

@Component
public final class JT808RegistrationPackageDecoder extends JT808PackageDecoder {
    private static final byte[] REQUIRED_PREFIX = {126, 1, 0};
    private static final int MANUFACTURER_ID_BYTE_COUNT = 5;

    public JT808RegistrationPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected Object decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        skipProvinceId(buffer);
        skipCityId(buffer);
        final String manufacturerId = decodeManufacturerId(buffer);
        return new JT808RegistrationPackage(phoneNumber, manufacturerId);
    }

    private void skipProvinceId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipCityId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private String decodeManufacturerId(final ByteBuf buffer) {
        return decodeString(buffer, MANUFACTURER_ID_BYTE_COUNT);
    }
}
