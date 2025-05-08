package by.vladzuev.locationreceiver.protocol.apel.decoder;

import by.vladzuev.locationreceiver.protocol.apel.model.login.ApelLoginRequestPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class ApelLoginRequestPackageDecoder extends ApelPackageDecoder {
    private static final Integer PREFIX = 11;

    public ApelLoginRequestPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected ApelLoginRequestPackage decodeStartingFromBody(final ByteBuf buffer) {
        return new ApelLoginRequestPackage();
    }
}
