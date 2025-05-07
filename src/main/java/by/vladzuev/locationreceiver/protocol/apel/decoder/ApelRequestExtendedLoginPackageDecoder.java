package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.login.ApelRequestExtendedLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class ApelRequestExtendedLoginPackageDecoder extends ApelPackageDecoder {
    private static final Short PREFIX = 12;

    public ApelRequestExtendedLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelRequestExtendedLoginPackage decodeStartingFromBody(final ByteBuf buffer) {
        skipTrackerId(buffer);
        skipSIM(buffer);
        final String imei = readImei(buffer);
        final String password = readPassword(buffer);
        return new ApelRequestExtendedLoginPackage(imei, password);
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
