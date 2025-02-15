package by.vladzuev.locationreceiver.protocol.wialon.decoder.location;

import by.vladzuev.locationreceiver.protocol.wialon.decoder.location.parser.WialonLocationParser;
import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.request.WialonRequestSingleLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestSingleLocationPackageDecoder extends WialonRequestLocationPackageDecoder {

    public WialonRequestSingleLocationPackageDecoder(final WialonLocationParser locationParser) {
        super(WialonRequestSingleLocationPackage.PREFIX, locationParser);
    }

    @Override
    protected WialonRequestSingleLocationPackage decodeMessageInternal(final String message,
                                                                       final WialonLocationParser locationParser) {
        final WialonLocation location = locationParser.parse(message);
        return new WialonRequestSingleLocationPackage(location);
    }
}
