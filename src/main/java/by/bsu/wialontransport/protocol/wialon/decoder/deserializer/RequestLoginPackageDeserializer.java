package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage.PREFIX;

@Component
public final class RequestLoginPackageDeserializer extends AbstractPackageDeserializer {
    private static final String REGEX_DELIMITER_IMEI_AND_PASSWORD = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    public RequestLoginPackageDeserializer() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestLoginPackage deserializeByMessage(final String message) {
        final String[] messageComponents = message.split(REGEX_DELIMITER_IMEI_AND_PASSWORD);
        final String imei = messageComponents[INDEX_IMEI];
        final String password = messageComponents[INDEX_PASSWORD];
        return new WialonRequestLoginPackage(imei, password);
    }
}