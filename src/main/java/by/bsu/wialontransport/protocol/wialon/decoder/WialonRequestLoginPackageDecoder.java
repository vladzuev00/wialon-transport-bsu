package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage.PREFIX;

@Component
public final class WialonRequestLoginPackageDecoder extends WialonPackageDecoder {
    private static final String COMPONENT_DELIMITER = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    public WialonRequestLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestLoginPackage decodeMessage(final String message) {
        final String[] components = message.split(COMPONENT_DELIMITER);
        final String imei = components[INDEX_IMEI];
        final String password = components[INDEX_PASSWORD];
        return new WialonRequestLoginPackage(imei, password);
    }
}
