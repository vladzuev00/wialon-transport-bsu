package by.bsu.wialontransport.protocol.wialon.decoder.deserializer;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage.PREFIX;

@Component
public final class RequestLoginPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DELIMITER_IMEI_AND_PASSWORD = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    @Override
    public RequestLoginPackage deserialize(final String source) {
        final String[] messageComponents = findMessageComponents(source);
        final String imei = messageComponents[INDEX_IMEI];
        final String password = messageComponents[INDEX_PASSWORD];
        return new RequestLoginPackage(imei, password);
    }

    private static String[] findMessageComponents(String deserialized) {
        final String message = PackageDeserializer.removePrefix(deserialized, PREFIX);
        return message.split(REGEX_DELIMITER_IMEI_AND_PASSWORD);
    }
}