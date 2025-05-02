package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
@RequiredArgsConstructor
public final class TeltonikaRequestLoginPackageDecoder extends TeltonikaPackageDecoder {
    private final LoginSuccessHolder loginSuccessHolder;

    @Override
    public boolean isAbleDecode(final ByteBuf buffer) {
        return !loginSuccessHolder.isSuccess();
    }

    @Override
    protected Object decodeInternal(final ByteBuf buffer) {
        final String imei = buffer.toString(US_ASCII);
        return new TeltonikaRequestLoginPackage(imei);
    }
}
