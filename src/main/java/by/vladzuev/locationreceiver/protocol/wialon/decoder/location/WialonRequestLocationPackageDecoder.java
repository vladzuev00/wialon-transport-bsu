package by.vladzuev.locationreceiver.protocol.wialon.decoder.location;

import by.vladzuev.locationreceiver.protocol.wialon.decoder.WialonPackageDecoder;
import by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonPackage;

public abstract class WialonRequestLocationPackageDecoder extends WialonPackageDecoder {
    private final WialonLocationParser locationParser;

    public WialonRequestLocationPackageDecoder(final String prefix, final WialonLocationParser locationParser) {
        super(prefix);
        this.locationParser = locationParser;
    }

    @Override
    protected final WialonPackage decodeMessage(final String message) {
        return decodeMessageInternal(message, locationParser);
    }

    protected abstract WialonPackage decodeMessageInternal(final String message,
                                                           final WialonLocationParser locationParser);
}
