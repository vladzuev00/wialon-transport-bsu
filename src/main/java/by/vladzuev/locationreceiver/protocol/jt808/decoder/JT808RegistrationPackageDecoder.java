package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import by.vladzuev.locationreceiver.protocol.jt808.model.JT808RegistrationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.protocol.jt808.util.JT808Util.decodeManufacturerId;

@Component
public final class JT808RegistrationPackageDecoder extends JT808PackageDecoder {
    private static final int IMEI_LENGTH_IN_PHONE_NUMBER = 10;

    private static final byte[] PREFIX = {126, 1, 0};

    public JT808RegistrationPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected JT808RegistrationPackage decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        skipProvinceId(buffer);
        skipCityId(buffer);
        final String manufacturerId = decodeManufacturerId(buffer);
        final String imei = createImei(manufacturerId, phoneNumber);
        return new JT808RegistrationPackage(imei);
    }

    private void skipProvinceId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipCityId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private String createImei(final String manufacturerId, final String phoneNumber) {
        return manufacturerId + phoneNumber.substring(phoneNumber.length() - IMEI_LENGTH_IN_PHONE_NUMBER);
    }
}
