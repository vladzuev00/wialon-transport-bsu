package by.bsu.wialontransport.protocol.wialon.decoder.location;

import by.bsu.wialontransport.protocol.wialon.decoder.WialonPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;

public abstract class WialonRequestLocationPackageDecoder extends WialonPackageDecoder {
    private final WialonLocationParser locationParser;

    public WialonRequestLocationPackageDecoder(final String requiredPrefix, final WialonLocationParser locationParser) {
        super(requiredPrefix);
        this.locationParser = locationParser;
    }

    @Override
    protected final WialonPackage decodeMessage(final String message) {
        return decodeMessageInternal(message, locationParser);
    }

    protected abstract WialonPackage decodeMessageInternal(final String message,
                                                           final WialonLocationParser locationParser);
}
