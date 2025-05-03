package by.vladzuev.locationreceiver.protocol.teltonika.decoder;

import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaRequestLoginPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public final class TeltonikaLoginPackageDecoder extends TeltonikaPackageDecoder {

    public TeltonikaLoginPackageDecoder(final LoginSuccessHolder loginSuccessHolder) {
        super(loginSuccessHolder, false);
    }

    @Override
    protected TeltonikaRequestLoginPackage decodeInternal(final ByteBuf buffer) {
        final String imei = buffer.toString(US_ASCII);
        return new TeltonikaRequestLoginPackage(imei);
    }
}
