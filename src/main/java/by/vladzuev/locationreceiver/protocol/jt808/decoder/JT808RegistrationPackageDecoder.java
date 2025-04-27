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
        final String imei = extractImei(buffer, phoneNumber);
        return new JT808RegistrationPackage(imei);
    }

    private void skipProvinceId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private void skipCityId(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
    }

    private String extractImei(final ByteBuf buffer, final String phoneNumber) {
        return decodeManufacturerId(buffer) + phoneNumber.substring(phoneNumber.length() - IMEI_LENGTH_IN_PHONE_NUMBER);
    }
}
