package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.login.ApelExtendedLoginRequestPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class ApelExtendedLoginRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 12;

    public ApelExtendedLoginRequestPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelExtendedLoginRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
        skipTrackerId(buffer);
        skipSIM(buffer);
        final String imei = readImei(buffer);
        final String password = readPassword(buffer);
        return new ApelExtendedLoginRequestPackage(imei, password);
    }

    private void skipTrackerId(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
    }

    private void skipSIM(final ByteBuf buffer) {
        final int length = buffer.readUnsignedShortLE();
        buffer.skipBytes(length);
    }

    private String readImei(final ByteBuf buffer) {
        final int length = buffer.readUnsignedShortLE();
        return buffer.readSlice(length).toString(US_ASCII);
    }

    private String readPassword(final ByteBuf buffer) {
        final int length = buffer.readUnsignedShortLE();
        return buffer.readSlice(length).toString(US_ASCII);
    }
}
