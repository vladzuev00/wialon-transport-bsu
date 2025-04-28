package by.vladzuev.locationreceiver.protocol.wialon.encoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.ping.WialonResponsePingPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonResponsePingPackageEncoder extends WialonPackageEncoder<WialonResponsePingPackage> {
    static final String ENCODED_MESSAGE = "";

    public WialonResponsePingPackageEncoder() {
        super(WialonResponsePingPackage.class);
    }

    @Override
    protected String getMessage(final WialonResponsePingPackage response) {
        return ENCODED_MESSAGE;
    }
}
