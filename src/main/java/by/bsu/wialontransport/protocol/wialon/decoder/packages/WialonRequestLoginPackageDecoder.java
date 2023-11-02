package by.bsu.wialontransport.protocol.wialon.decoder.packages;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonRequestLoginPackage.PREFIX;

public final class WialonRequestLoginPackageDecoder extends WialonPackageDecoder<WialonRequestLoginPackage> {
    private static final String REGEX_DELIMITER_IMEI_AND_PASSWORD = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    public WialonRequestLoginPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected WialonRequestLoginPackage decodeMessage(final String message) {
        final String[] messageComponents = message.split(REGEX_DELIMITER_IMEI_AND_PASSWORD);
        final String imei = messageComponents[INDEX_IMEI];
        final String password = messageComponents[INDEX_PASSWORD];
        return new WialonRequestLoginPackage(imei, password);
    }
}
