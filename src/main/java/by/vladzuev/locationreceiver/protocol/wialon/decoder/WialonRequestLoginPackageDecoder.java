package by.vladzuev.locationreceiver.protocol.wialon.decoder;

import by.vladzuev.locationreceiver.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestLoginPackageDecoder extends WialonPackageDecoder {
    private static final String COMPONENT_DELIMITER = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    public WialonRequestLoginPackageDecoder() {
        super(WialonRequestLoginPackage.PREFIX);
    }

    @Override
    protected WialonRequestLoginPackage decodeMessage(final String message) {
        final String[] components = message.split(COMPONENT_DELIMITER);
        final String imei = components[INDEX_IMEI];
        final String password = components[INDEX_PASSWORD];
        return new WialonRequestLoginPackage(imei, password);
    }
}
