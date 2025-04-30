package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.core.decoder.packages.PackageDecoder;
import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
@RequiredArgsConstructor
public final class TeltonikaRequestLoginPackageDecoder implements PackageDecoder<ByteBuf> {
    private final LoginSuccessHolder loginSuccessHolder;

    @Override
    public boolean isAbleDecode(final ByteBuf buffer) {
        return !loginSuccessHolder.isSuccess();
    }

    @Override
    public TeltonikaRequestLoginPackage decode(final ByteBuf buffer) {
        final String imei = decodeImei(buffer);
        return new TeltonikaRequestLoginPackage(imei);
    }

    private String decodeImei(final ByteBuf buffer) {
        buffer.skipBytes(Short.BYTES);
        return buffer.toString(US_ASCII);
    }
}
